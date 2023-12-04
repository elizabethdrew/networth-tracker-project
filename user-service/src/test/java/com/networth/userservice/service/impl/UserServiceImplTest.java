package com.networth.userservice.service.impl;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.util.HelperUtils;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HelperUtils helperUtils;

    @Mock
    private KeycloakClient keycloakClient;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserOutput expectedOutput;

    private RegisterDto testRegister;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createTestUser();
        createTestOutputData();
        createTestRegisterData();
    }

    private void createTestUser() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setKeycloakId("0c77e0c4-95f4-4704-a24f-ee22deb43609");
        testUser.setUsername("seeduser");
        testUser.setEmail("seeduser@example.co.uk");
        testUser.setActiveUser(true);
        testUser.setTaxRate(null);
        testUser.setDateOfBirth(null);
        testUser.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        testUser.setDateUpdated(null);
    }

    private void createTestOutputData() {
        expectedOutput = new UserOutput();
        expectedOutput.setUserId(1L);
        expectedOutput.setUsername("seeduser");
        expectedOutput.setEmail("seeduser@example.co.uk");
        expectedOutput.setDateOfBirth(null);
        expectedOutput.setTaxRate(null);
        expectedOutput.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        expectedOutput.setDateUpdated(null);
        expectedOutput.setActiveUser(true);
    }

    private void createTestRegisterData() {
        testRegister = new RegisterDto();
        testRegister.setUsername("seeduser");
        testRegister.setEmail("seeduser@example.co.uk");
        testRegister.setPassword("Password123!");
    }


    @Test
    void getUser_WhenUserExists_ReturnsUserOutput() {
        String keycloakId = testUser.getKeycloakId();

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(testUser));
        when(userMapper.toUserOutput(testUser)).thenReturn(expectedOutput);

        UserOutput result = userService.getUser(keycloakId);

        assertEquals(expectedOutput, result);
        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper).toUserOutput(testUser);
    }

    @Test
    void getUser_WhenUserNotFound_ThrowsUserNotFoundException() {
        String keycloakId = "nonexistentId";

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(keycloakId));
        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper, never()).toUserOutput(any());
    }

    @Test
    void testRegisterUser_Success() {

        when(userRepository.existsByUsername(testRegister.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(testRegister.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toUserOutput(testUser)).thenReturn(expectedOutput);

        // Mocking successful response from Keycloak
        Request request = Request.create(Request.HttpMethod.POST, "", Collections.emptyMap(), null, new RequestTemplate());
        Response mockResponse = Response.builder()
                .status(HttpStatus.CREATED.value())
                .request(request)
                .headers(Collections.singletonMap("Location", Collections.singletonList("http://keycloak:8080/users/12345")))
                .build();
        when(keycloakClient.createKeycloakUser(any(), any())).thenReturn(mockResponse);

        // Mocking access token retrieval
        when(helperUtils.getAdminAccessToken()).thenReturn("token");

        UserOutput result = userService.registerUser(testRegister);

        assertEquals(expectedOutput, result);
        verify(userRepository).save(any(User.class));
        verify(keycloakClient).createKeycloakUser(any(), any());
        verify(userMapper).toUserOutput(testUser);
    }

    @Test
    void testRegisterUser_UsernameExists_ThrowsException() {
        testRegister.setUsername("existingUser");

        when(userRepository.existsByUsername(testRegister.getUsername())).thenReturn(true);

        assertThrows(InvalidInputException.class, () -> userService.registerUser(testRegister));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailExists_ThrowsException() {
        testRegister.setEmail("seeduser@example.co.uk");

        when(userRepository.existsByEmail(testRegister.getEmail())).thenReturn(true);

        assertThrows(InvalidInputException.class, () -> userService.registerUser(testRegister));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_KeycloakFailure_ThrowsException() {

        when(userRepository.existsByUsername(testRegister.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(testRegister.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toUserOutput(testUser)).thenReturn(expectedOutput);

        Request failedRequest = Request.create(Request.HttpMethod.POST, "", Collections.emptyMap(), null, new RequestTemplate());
        Response failedMockResponse = Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .request(failedRequest)
                .build();
        when(keycloakClient.createKeycloakUser(any(), any())).thenReturn(failedMockResponse);

        // Mocking access token retrieval
        when(helperUtils.getAdminAccessToken()).thenReturn("token");

        assertThrows(KeycloakException.class, () -> userService.registerUser(testRegister));
        verify(userRepository, never()).save(any(User.class));
    }


}

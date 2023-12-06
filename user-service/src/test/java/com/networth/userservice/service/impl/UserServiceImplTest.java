package com.networth.userservice.service.impl;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.TaxRate;
import com.networth.userservice.dto.UpdateKeycloakDto;
import com.networth.userservice.dto.UpdateUserDto;
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
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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

    private User testUpdatedUser;
    private UserOutput expectedOutput;
    private UserOutput expectedUpdateOutput;
    private UpdateUserDto testUpdateUserDto;
    private RegisterDto testRegister;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createTestUser();
        createTestUpdatedUser();
        createTestOutputData();
        createTestRegisterData();
        createTestUpdateData();
        createTestUpdateUserDto();
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

    private void createTestUpdatedUser() {
        testUpdatedUser = new User();
        testUpdatedUser.setUserId(1L);
        testUpdatedUser.setKeycloakId("0c77e0c4-95f4-4704-a24f-ee22deb43609");
        testUpdatedUser.setUsername("seeduser");
        testUpdatedUser.setEmail("seeduser@example.co.uk");
        testUpdatedUser.setActiveUser(true);
        testUpdatedUser.setTaxRate(TaxRate.valueOf("BASIC"));
        testUpdatedUser.setDateOfBirth(LocalDate.parse("1986-09-02"));
        testUpdatedUser.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        testUpdatedUser.setDateUpdated(LocalDateTime.parse("2023-06-06T12:00:00"));
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

    private void createTestUpdateData() {
        expectedUpdateOutput = new UserOutput();
        expectedUpdateOutput.setUserId(1L);
        expectedUpdateOutput.setUsername("seeduser");
        expectedUpdateOutput.setEmail("seeduser@example.co.uk");
        expectedUpdateOutput.setDateOfBirth(LocalDate.parse("1986-09-02"));
        expectedUpdateOutput.setTaxRate(TaxRate.valueOf("BASIC"));
        expectedUpdateOutput.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        expectedUpdateOutput.setDateUpdated(LocalDateTime.parse("2023-06-06T12:00:00"));
        expectedUpdateOutput.setActiveUser(true);
    }

    private void createTestUpdateUserDto() {
        testUpdateUserDto = new UpdateUserDto();
        testUpdateUserDto.setEmail("seeduser@example.co.uk");
        testUpdateUserDto.setDateOfBirth(LocalDate.parse("1986-09-02"));
        testUpdateUserDto.setTaxRate(TaxRate.valueOf("BASIC"));
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

    @Test
    void testUpdateUser_EmailSame_Success() {
        String keycloakId = testUser.getKeycloakId();

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(testUser));
        doAnswer(invocation -> {
            User u = invocation.getArgument(1);
            u.setDateOfBirth(testUpdateUserDto.getDateOfBirth());
            u.setTaxRate(testUpdateUserDto.getTaxRate());
            return null;
        }).when(userMapper).updateUserFromDto(eq(testUpdateUserDto), any(User.class));

        when(userRepository.save(any(User.class))).thenReturn(testUpdatedUser);

        when(userMapper.toUserOutput(testUpdatedUser)).thenReturn(expectedUpdateOutput);

        UserOutput result = userService.updateUser(keycloakId, testUpdateUserDto);

        assertEquals(expectedUpdateOutput, result);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserOutput(testUpdatedUser);
        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper).updateUserFromDto(eq(testUpdateUserDto), any(User.class));
    }

    @Test
    void testUpdateUser_EmailDifferent_Success() {
        String keycloakId = testUser.getKeycloakId();
        String newEmail = "newemail@example.com";
        testUpdateUserDto.setEmail(newEmail);

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(testUser));
        doAnswer(invocation -> {
            User u = invocation.getArgument(1);
            u.setEmail(testUpdateUserDto.getEmail());
            u.setDateOfBirth(testUpdateUserDto.getDateOfBirth());
            u.setTaxRate(testUpdateUserDto.getTaxRate());
            return null;
        }).when(userMapper).updateUserFromDto(eq(testUpdateUserDto), any(User.class));

        when(userRepository.save(any(User.class))).thenReturn(testUpdatedUser);

        // Mock successful response from Keycloak for email update
        Response keycloakResponse = Response.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .request(Request.create(Request.HttpMethod.PUT, "", Collections.emptyMap(), null, new RequestTemplate()))
                .build();
        when(keycloakClient.updateKeycloakUser(any(), eq(keycloakId), any(UpdateKeycloakDto.class))).thenReturn(keycloakResponse);
        when(helperUtils.getAdminAccessToken()).thenReturn("token");

        when(userMapper.toUserOutput(testUpdatedUser)).thenReturn(expectedUpdateOutput);

        UserOutput result = userService.updateUser(keycloakId, testUpdateUserDto);

        assertEquals(expectedUpdateOutput, result);
        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper).updateUserFromDto(eq(testUpdateUserDto), any(User.class));
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserOutput(testUpdatedUser);
        verify(keycloakClient).updateKeycloakUser(any(), eq(keycloakId), any(UpdateKeycloakDto.class));
    }



}

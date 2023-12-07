package com.networth.userservice.service.impl;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.TaxRate;
import com.networth.userservice.dto.UpdateUserDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.KeycloakService;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.util.HelperUtils;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private HelperUtils helperUtils;
    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private UserServiceImpl userService;

    private final String keycloakId = "test-keycloak-id";
    private User mockUser;
    private UserOutput mockUserOutput;

    private UpdateUserDto mockUpdateUserDto;

    private RegisterDto mockRegisterDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setKeycloakId("0c77e0c4-95f4-4704-a24f-ee22deb43609");
        mockUser.setUsername("seeduser");
        mockUser.setEmail("seeduser@example.co.uk");
        mockUser.setActiveUser(true);
        mockUser.setTaxRate(null);
        mockUser.setDateOfBirth(null);
        mockUser.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        mockUser.setDateUpdated(null);

        mockUserOutput = new UserOutput();
        mockUserOutput.setUserId(1L);
        mockUserOutput.setUsername("seeduser");
        mockUserOutput.setEmail("seeduser@example.co.uk");
        mockUserOutput.setDateOfBirth(null);
        mockUserOutput.setTaxRate(null);
        mockUserOutput.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        mockUserOutput.setDateUpdated(null);
        mockUserOutput.setActiveUser(true);

        mockUpdateUserDto = new UpdateUserDto();
        mockUpdateUserDto.setEmail("seeduser@example.co.uk");
        mockUpdateUserDto.setDateOfBirth(LocalDate.parse("1986-09-02"));
        mockUpdateUserDto.setTaxRate(TaxRate.valueOf("BASIC"));

        mockRegisterDto = new RegisterDto();
        mockRegisterDto.setUsername("seeduser");
        mockRegisterDto.setEmail("seeduser@example.co.uk");
        mockRegisterDto.setPassword("Password123!");

    }

    @Test
    public void testRegisterUserSuccess() {
        // Setup
        Response mockResponse = mock(Response.class);
        when(keycloakService.createUser(mockRegisterDto)).thenReturn(mockResponse);
        when(keycloakService.extractKeycloakUserId(mockResponse)).thenReturn(Optional.of("keycloakUserId"));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userMapper.toUserOutput(any(User.class))).thenReturn(mockUserOutput);

        // Act
        UserOutput result = userService.registerUser(mockRegisterDto);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(keycloakService).createUser(mockRegisterDto);
        verify(keycloakService).extractKeycloakUserId(mockResponse);
    }

    @Test
    public void testRegisterEmailValidationFailure() {
        doThrow(new InvalidInputException("Email already exists")).when(helperUtils).validateEmailUnique(mockRegisterDto.getEmail());

        // Act & Assert
        assertThrows(InvalidInputException.class, () -> userService.registerUser(mockRegisterDto));
    }

    @Test
    public void testRegisterUsernameValidationFailure() {
        doThrow(new InvalidInputException("Username already exists")).when(helperUtils).validateUsernameUnique(mockRegisterDto.getUsername());

        // Act & Assert
        assertThrows(InvalidInputException.class, () -> userService.registerUser(mockRegisterDto));
    }

    @Test
    public void testRegisterUserKeycloakFailure() {
        when(keycloakService.createUser(mockRegisterDto)).thenThrow(new KeycloakException("Keycloak error"));

        // Act & Assert
        assertThrows(KeycloakException.class, () -> userService.registerUser(mockRegisterDto));
    }

    @Test
    public void testGetUserSuccess() {
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserOutput(mockUser)).thenReturn(mockUserOutput);

        UserOutput result = userService.getUser(keycloakId);

        assertEquals(mockUserOutput, result);
        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper).toUserOutput(mockUser);
    }

    @Test
    public void testGetUserNotFound() {
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(keycloakId));

        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper, never()).toUserOutput(any(User.class));
    }

    @Test
    public void testUpdateUserSuccess() {
        // Setup
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userMapper.toUserOutput(any(User.class))).thenReturn(mockUserOutput);

        // Act
        UserOutput result = userService.updateUser(keycloakId, mockUpdateUserDto);

        // Assert
        assertEquals(mockUserOutput, result);
        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper).updateUserFromDto(mockUpdateUserDto, mockUser);
        verify(userRepository).save(mockUser);
    }

    @Test
    public void testUpdateUserNotFound() {
        // Setup
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(keycloakId, mockUpdateUserDto));

        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userMapper, never()).updateUserFromDto(any(UpdateUserDto.class), any(User.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUserEmailChange() {
        // Setup
        mockUpdateUserDto.setEmail("newemail@example.com");
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userMapper.toUserOutput(any(User.class))).thenReturn(mockUserOutput);

        // Act
        userService.updateUser(keycloakId, mockUpdateUserDto);

        // Assert
        verify(helperUtils).validateEmailUnique("newemail@example.com");
        verify(keycloakService).updateEmailKeycloak("newemail@example.com", keycloakId);
    }

    @Test
    public void testDeleteUserSuccess() {
        // Setup
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(mockUser));

        // Act
        userService.deleteUser(keycloakId);

        // Assert
        assertFalse(mockUser.getActiveUser());
        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userRepository).save(mockUser);
    }

    @Test
    public void testDeleteUserNotFound() {
        // Setup
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(keycloakId));

        verify(userRepository).findByKeycloakId(keycloakId);
        verify(userRepository, never()).save(any(User.class));
    }
}


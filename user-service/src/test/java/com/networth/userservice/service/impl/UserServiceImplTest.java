package com.networth.userservice.service.impl;

import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.KeycloakService;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.util.HelperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}


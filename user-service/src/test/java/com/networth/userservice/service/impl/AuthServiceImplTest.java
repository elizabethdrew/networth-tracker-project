package com.networth.userservice.service.impl;

import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.AuthenticationServiceException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.mapper.TokenResponseMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.KeycloakService;
import com.networth.userservice.util.HelperUtils;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private HelperUtils helperUtils;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private TokenResponseMapper tokenResponseMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginDto mockLoginDto;
    private User mockUser;
    private TokenResponse mockTokenResponse;
    private LoginResponse mockLoginResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockLoginDto = new LoginDto();
        mockLoginDto.setUsername("seeduser");
        mockLoginDto.setPassword("Password123!");

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

        mockTokenResponse = new TokenResponse();
        mockTokenResponse.setAccessToken("mockAccessToken");
        mockTokenResponse.setExpiresIn(300);
        mockTokenResponse.setRefreshExpiresIn(300);
        mockTokenResponse.setRefreshToken("mockRefreshToken");
        mockTokenResponse.setTokenType("mockTokenType");
        mockTokenResponse.setScope("mockScope");
        mockTokenResponse.setIdToken("mockIdToken");
        mockTokenResponse.setSessionState("mockSessionState");
        mockTokenResponse.setNotBeforePolicy(1);

        mockLoginResponse = new LoginResponse();
        mockLoginResponse.setUserId(1L);
    }

    @Test
    public void testUserLoginSuccess() {
        when(userRepository.findByUsername(mockLoginDto.getUsername())).thenReturn(Optional.of(mockUser));
        when(keycloakService.getUserAccessToken(mockLoginDto)).thenReturn(mockTokenResponse);
        when(tokenResponseMapper.tokenResponseToLoginResponse(mockTokenResponse)).thenReturn(mockLoginResponse);

        LoginResponse result = authService.userLogin(mockLoginDto);

        assertNotNull(result);
        assertEquals(mockLoginResponse, result);
        verify(userRepository).findByUsername(mockLoginDto.getUsername());
        verify(keycloakService).getUserAccessToken(mockLoginDto);
        verify(tokenResponseMapper).tokenResponseToLoginResponse(mockTokenResponse);
    }

    @Test
    public void testUserLoginUserNotFound() {
        when(userRepository.findByUsername(mockLoginDto.getUsername())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.userLogin(mockLoginDto));

        verify(userRepository).findByUsername(mockLoginDto.getUsername());
        verify(helperUtils, never()).validatePassword(anyString());
        verify(keycloakService, never()).getUserAccessToken(any(LoginDto.class));
        verify(tokenResponseMapper, never()).tokenResponseToLoginResponse(any(TokenResponse.class));
    }

    @Test
    public void testUserLoginKeycloakFailure() {
        when(userRepository.findByUsername(mockLoginDto.getUsername())).thenReturn(Optional.of(mockUser));
        when(keycloakService.getUserAccessToken(mockLoginDto)).thenThrow(FeignException.FeignClientException.errorStatus(
                "KeycloakService#getUserAccessToken(LoginDto)",
                Response.builder().request(Request.create(Request.HttpMethod.POST, "", Collections.emptyMap(), null, Charset.defaultCharset(), null)).status(400).build()));

        assertThrows(AuthenticationServiceException.class, () -> authService.userLogin(mockLoginDto));

        verify(userRepository).findByUsername(mockLoginDto.getUsername());
        verify(keycloakService).getUserAccessToken(mockLoginDto);
        verify(tokenResponseMapper, never()).tokenResponseToLoginResponse(any(TokenResponse.class));
    }
}

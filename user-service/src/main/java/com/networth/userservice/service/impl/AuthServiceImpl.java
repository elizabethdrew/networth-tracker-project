package com.networth.userservice.service.impl;

import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.AuthenticationServiceException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.exception.UserServiceException;
import com.networth.userservice.mapper.TokenResponseMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.AuthService;
import com.networth.userservice.service.KeycloakService;
import com.networth.userservice.util.HelperUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final HelperUtils helperUtils;
    private final KeycloakService keycloakService;
    private final TokenResponseMapper tokenResponseMapper;

    public AuthServiceImpl(UserRepository userRepository, HelperUtils helperUtils, KeycloakService keycloakService, TokenResponseMapper tokenResponseMapper) {
        this.userRepository = userRepository;
        this.helperUtils = helperUtils;
        this.keycloakService = keycloakService;
        this.tokenResponseMapper = tokenResponseMapper;
    }

    public LoginResponse userLogin(LoginDto loginDto) {

        log.debug("Attempting to log in user: {}", loginDto.getUsername());

        // Check if username exists
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login failed: Username '{}' not found.", loginDto.getUsername());
                    return new UserNotFoundException("User not found with Username: " + loginDto.getUsername());
                });

        log.debug("Username check passed for: {}", loginDto.getUsername());

        // Validate Password
        helperUtils.validatePassword(loginDto.getPassword());

        try {
            TokenResponse tokenResponse = keycloakService.getUserAccessToken(loginDto);
            LoginResponse loginResponse = tokenResponseMapper.tokenResponseToLoginResponse(tokenResponse);
            loginResponse.setUserId(user.getUserId());
            return loginResponse;
        } catch (FeignException e) {
            log.error("Keycloak token retrieval failed for user: {}", loginDto.getUsername(), e);
            throw new AuthenticationServiceException("Could not obtain access token from Keycloak.", e);
        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}", loginDto.getUsername(), e);
            throw new UserServiceException("Unexpected error during login process.", e);
        }
    }

    public void userLogout(LogoutDto logoutDto) {

        // Revoke the access token first
        if (logoutDto.getAccessToken() != null && !logoutDto.getAccessToken().isEmpty()) {
            keycloakService.revokeAccessToken(logoutDto.getAccessToken());
        }

        // Then logout the user, which invalidates the refresh token
        if (logoutDto.getRefreshToken() != null && !logoutDto.getRefreshToken().isEmpty()) {
            keycloakService.logoutUser(logoutDto);
        }
    }

}

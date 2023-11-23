package com.networth.userservice.service.impl;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.AuthenticationServiceException;
import com.networth.userservice.exception.InvalidCredentialsException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.exception.UserServiceException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.AuthService;
import com.networth.userservice.util.HelperUtils;
import feign.Feign;
import feign.FeignException;
import feign.Response;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final HelperUtils helperUtils;
    private final KeycloakProperties keycloakProperties;

    public AuthServiceImpl(UserRepository userRepository, HelperUtils helperUtils, KeycloakProperties keycloakProperties) {
        this.userRepository = userRepository;
        this.helperUtils = helperUtils;
        this.keycloakProperties = keycloakProperties;
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
        if (!helperUtils.validatePassword(loginDto.getPassword())) {
            log.warn("Login failed: Invalid password.");
            throw new InvalidCredentialsException("Invalid password.");
        }

        log.debug("Password validation passed for: {}", loginDto.getUsername());


        try {
            TokenResponse tokenResponse = helperUtils.getUserAccessToken(loginDto);
            log.debug("Token obtained for user: {}", loginDto.getUsername());

            // Create the LoginResponse with the additional userId
            LoginResponse loginResponse = new LoginResponse();
            BeanUtils.copyProperties(tokenResponse, loginResponse);
            loginResponse.setUserId(user.getUserId());

            log.info("User '{}' logged in successfully.", loginDto.getUsername());
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
        if (logoutDto.getAccess_token() != null && !logoutDto.getAccess_token().isEmpty()) {
            revokeAccessToken(logoutDto.getAccess_token());
        }

        // Then logout the user, which invalidates the refresh token
        if (logoutDto.getRefresh_token() != null && !logoutDto.getRefresh_token().isEmpty()) {
            logoutUser(logoutDto);
        }
    }

    private void revokeAccessToken(String accessToken) {
        log.debug("Revoking access token for user");

        KeycloakClient keycloakRevokeClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(KeycloakClient.class, keycloakProperties.getBaseUri());

        try {
            Response response = keycloakRevokeClient.keycloakRevoke(buildRevokeData(accessToken));
            handleKeycloakResponse(response, "Access token revocation failed");
            log.info("Access token revoked successfully");
        } catch (FeignException e) {
            log.error("Keycloak revocation endpoint responded with an error", e);
            throw new AuthenticationServiceException("Failed to revoke access token in Keycloak", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during access token revocation", e);
            throw new UserServiceException("Unexpected error during access token revocation", e);
        }
    }

    private void logoutUser(LogoutDto logoutDto) {
        log.debug("Logging out user from Keycloak");

        KeycloakClient keycloakLogoutClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(KeycloakClient.class, keycloakProperties.getBaseUri());

        try {
            Response response = keycloakLogoutClient.keycloakLogout(buildLogoutData(logoutDto));
            handleKeycloakResponse(response, "User logout failed");
            log.info("User logged out successfully");
        } catch (FeignException e) {
            log.error("Keycloak logout endpoint responded with an error", e);
            throw new AuthenticationServiceException("Failed to logout user from Keycloak", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during user logout", e);
            throw new UserServiceException("Unexpected error during user logout", e);
        }
    }

    private KeycloakAccessDto buildRevokeData(String accessToken) {
        KeycloakAccessDto revokeData = new KeycloakAccessDto();
        revokeData.setClient_id(keycloakProperties.getKeyUser().getClientId());
        revokeData.setClient_secret(keycloakProperties.getKeyUser().getClientSecret());
        revokeData.setToken(accessToken);
        return revokeData;
    }

    private KeycloakAccessDto buildLogoutData(LogoutDto logoutDto) {
        KeycloakAccessDto formData = new KeycloakAccessDto();
        formData.setClient_id(keycloakProperties.getKeyUser().getClientId());
        formData.setClient_secret(keycloakProperties.getKeyUser().getClientSecret());
        formData.setRefresh_token(logoutDto.getRefresh_token());
        formData.setId_token_hint(logoutDto.getId_token_hint());
        formData.setPost_logout_redirect_uri(keycloakProperties.getLogoutRedirectUrl());
        return formData;
    }

    private void handleKeycloakResponse(Response response, String errorMessage) {
        if (response.status() < 200 || response.status() >= 300) {
            log.error(errorMessage + ". Status: {}, Body: {}", response.status(), response.body());
            throw new KeycloakException(errorMessage + ". Status: " + response.status());
        }
    }

//    public void userLogout(LogoutDto logoutDto) {
//
//        log.info("Starting Revoke");
//
//        KeycloakClient keycloakRevokeClient = Feign.builder()
//                .encoder(new FormEncoder(new JacksonEncoder()))
//                .decoder(new JacksonDecoder())
//                .target(KeycloakClient.class, keycloakProperties.getBaseUri());
//
//        // Create the logout form data
//        KeycloakAccessDto revokeData = new KeycloakAccessDto();
//        revokeData.setClient_id(keycloakProperties.getKeyUser().getClientId());
//        revokeData.setClient_secret(keycloakProperties.getKeyUser().getClientSecret());
//        revokeData.setToken(logoutDto.getAccess_token());
//
//        log.info(String.valueOf(revokeData));
//
//        try {
//            Response response = keycloakRevokeClient.keycloakRevoke(revokeData);
//
//            log.info(String.valueOf(response));
//
//            if (response.status() < 200 || response.status() >= 300) {
//                throw new KeycloakException("Failed to logout user. Status: " + response.status(), e);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to logout from Keycloak", e);
//        }
//
//        log.info("Starting Logout");
//
//        KeycloakClient keycloakLogoutClient = Feign.builder()
//                .encoder(new FormEncoder(new JacksonEncoder()))
//                .decoder(new JacksonDecoder())
//                .target(KeycloakClient.class, keycloakProperties.getBaseUri());
//
//        // Create the logout form data
//        KeycloakAccessDto formData = new KeycloakAccessDto();
//        formData.setClient_id(keycloakProperties.getKeyUser().getClientId());
//        formData.setClient_secret(keycloakProperties.getKeyUser().getClientSecret());
//        formData.setRefresh_token(logoutDto.getRefresh_token());
//        formData.setId_token_hint(logoutDto.getId_token_hint());
//        formData.setPost_logout_redirect_uri(keycloakProperties.getLogoutRedirectUrl());
//
//        log.info(String.valueOf(formData));
//
//        try {
//            Response response = keycloakLogoutClient.keycloakLogout(formData);
//
//            log.info(String.valueOf(response));
//
//            if (response.status() < 200 || response.status() >= 300) {
//                throw new KeycloakException("Failed to logout user. Status: " + response.status(), e);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to logout from Keycloak", e);
//        }
//
//
//    }

}

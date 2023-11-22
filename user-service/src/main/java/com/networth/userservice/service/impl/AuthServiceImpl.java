package com.networth.userservice.service.impl;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.AuthService;
import com.networth.userservice.util.HelperUtils;
import feign.Feign;
import feign.Response;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final HelperUtils helperUtils;
    private final RestTemplate restTemplate;
    private final KeycloakProperties keycloakProperties;

    public AuthServiceImpl(UserRepository userRepository, HelperUtils helperUtils, RestTemplate restTemplate, KeycloakProperties keycloakProperties) {
        this.userRepository = userRepository;
        this.helperUtils = helperUtils;
        this.restTemplate = restTemplate;
        this.keycloakProperties = keycloakProperties;
    }

    public LoginResponse userLogin(LoginDto loginDto) {

        log.info("User Login Started");

        // Check if username exists
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with Username: " + loginDto.getUsername()));

        log.info("Username Passed Checks");

        // Validate Password
        helperUtils.validatePassword(loginDto.getPassword());

        log.info("Password Passed Checks");

        TokenResponse tokenResponse = helperUtils.getUserAccessToken(loginDto);

        // Create the LoginResponse with the additional userId
        LoginResponse loginResponse = new LoginResponse();
        BeanUtils.copyProperties(tokenResponse, loginResponse);
        loginResponse.setUserId(user.getUserId()); // Assuming there's a field `id` in User entity

        log.info("User logged in successfully");

        // Return the LoginResponse
        return loginResponse;
    }

    public void userLogout(LogoutDto logoutDto) {

        log.info("Starting Logout");

        KeycloakClient keycloakClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(KeycloakClient.class, keycloakProperties.getBaseUri());

        // Create the logout form data
        KeycloakAccessDto formData = new KeycloakAccessDto();
        formData.setClient_id(keycloakProperties.getKeyUser().getClientId());
        formData.setClient_secret(keycloakProperties.getKeyUser().getClientSecret());
        formData.setRefresh_token(logoutDto.getRefresh_token());
        formData.setId_token_hint(logoutDto.getId_token_hint());
        formData.setPost_logout_redirect_uri(keycloakProperties.getLogoutRedirectUrl());

        log.info(String.valueOf(formData));

        try {
            Response response = keycloakClient.keycloakLogout(formData);

            log.info(String.valueOf(response));

            if (response.status() < 200 || response.status() >= 300) {
                throw new KeycloakException("Failed to logout user. Status: " + response.status());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to logout from Keycloak", e);
        }
    }

}

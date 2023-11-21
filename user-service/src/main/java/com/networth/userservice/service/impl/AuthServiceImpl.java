package com.networth.userservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.AuthService;
import com.networth.userservice.util.HelperUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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

    @Transactional
    public LoginResponse userLogin(LoginDto loginDto) {

        log.info("User Login Started");

        // Check if username exists
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with Username: " + loginDto.getUsername()));

        log.info("Username Passed Checks");

        // Validate Password
        helperUtils.validatePassword(loginDto.getPassword());

        log.info("Password Passed Checks");

        // Get User Access Token

        // Create the request body
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.getKeyUser().getClientId());
        formData.add("client_secret", keycloakProperties.getKeyUser().getClientSecret());
        formData.add("grant_type", "password");
        formData.add("scope", "openid email profile");
        formData.add("username", loginDto.getUsername());
        formData.add("password", loginDto.getPassword());

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the HttpEntity
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        try {
            // Execute the POST request
            ResponseEntity<String> response = restTemplate.postForEntity(
                    keycloakProperties.getBaseUri() + "/realms/" + keycloakProperties.getKeyUser().getRealm() + "/protocol/openid-connect/token",
                    entity,
                    String.class);

            // Check for error response
            if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                log.error("Error response from Keycloak: Status Code: {}, Body: {}", response.getStatusCode(), response.getBody());
                throw new KeycloakException("Error response from Keycloak: Status Code: " +
                        response.getStatusCode() + ", Body: " + response.getBody());
            }

            // Map Response to TokenResponse
            ObjectMapper objectMapper = new ObjectMapper();
            TokenResponse tokenResponse = objectMapper.readValue(response.getBody(), TokenResponse.class);

            // Create the LoginResponse with the additional userId
            LoginResponse loginResponse = new LoginResponse();
            BeanUtils.copyProperties(tokenResponse, loginResponse);
            loginResponse.setUserId(user.getUserId()); // Assuming there's a field `id` in User entity

            log.info("User logged in successfully");

            // Return the LoginResponse
            return loginResponse;

        } catch (RestClientException e) {
            log.error("Failed to retrieve access token", e);
            throw new RuntimeException("Failed to retrieve access token", e);
        } catch (IOException e) {
            log.error("Error mapping Keycloak response to TokenResponse", e);
            throw new RuntimeException("Error mapping Keycloak response to TokenResponse", e);
        }
    }
}

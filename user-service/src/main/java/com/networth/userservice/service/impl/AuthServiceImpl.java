package com.networth.userservice.service.impl;

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

    public void userLogout(String refreshToken) {

        // Create the logout request for Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.getKeyUser().getClientId());
        formData.add("client_secret", keycloakProperties.getKeyUser().getClientSecret());
        formData.add("refresh_token", refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        try {
            // Send the logout request to Keycloak
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    keycloakProperties.getBaseUri() + "/realms/" + keycloakProperties.getKeyUser().getRealm() + "/protocol/openid-connect/logout",
                    request,
                    Void.class);

            if (response.getStatusCode().isError()) {
                log.error("Error logging out from Keycloak: Status Code: {}, Body: {}", response.getStatusCode(), response.getBody());
                throw new KeycloakException("Error logging out from Keycloak: Status Code: " + response.getStatusCode());
            }

            log.info("User logged out successfully");
        } catch (RestClientException e) {
            log.error("Logout request to Keycloak failed", e);
            throw new RuntimeException("Logout request to Keycloak failed", e);
        }
    }

}

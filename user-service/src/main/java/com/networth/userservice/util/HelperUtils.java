package com.networth.userservice.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HelperUtils {

    private final RestTemplate restTemplate;
    private final KeycloakProperties keycloakProperties;

    public HelperUtils(RestTemplate restTemplate, KeycloakProperties keycloakProperties) {
        this.restTemplate = restTemplate;
        this.keycloakProperties = keycloakProperties;
    }


    // Validate Password Against Rules
    public void validatePassword(String password) {

        // Define rules
        List<Rule> rules = new ArrayList<>();
        rules.add(new LengthRule(8, 100)); // password length between 8 and 100
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1)); // at least 1 uppercase character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1)); // at least 1 lowercase character
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1)); // at least 1 digit
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1)); // at least 1 special character

        PasswordValidator validator = new PasswordValidator(rules);
        RuleResult result = validator.validate(new PasswordData(password));

        if (!result.isValid()) {
            String message = String.join(", ", validator.getMessages(result));
            throw new InvalidInputException("Invalid password: " + message);
        }

    }

    // Get Keycloak Admin Access Token
    public String getAdminAccessToken() {
        log.info("Keycloak Admin Access Flow Started");

        // Create the request body
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.getAdmin().getClientId());
        formData.add("grant_type", "password");
        formData.add("username", keycloakProperties.getAdmin().getUsername());
        formData.add("password", keycloakProperties.getAdmin().getPassword());

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the HttpEntity
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        try {
            // Execute the POST request
            ResponseEntity<String> response = restTemplate.postForEntity(
                    keycloakProperties.getBaseUri() + "/realms/" + keycloakProperties.getAdmin().getRealm() + "/protocol/openid-connect/token",
                    entity,
                    String.class);

            // Check for error response
            if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                log.error("Error response from Keycloak: Status Code: {}, Body: {}", response.getStatusCode(), response.getBody());
                throw new KeycloakException("Error response from Keycloak: Status Code: " +
                        response.getStatusCode() + ", Body: " + response.getBody());
            }

            // Parse the access token from the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String accessToken = jsonNode.get("access_token").asText();
            log.info("Extracted access token");

            return accessToken;
        } catch (RestClientException | IOException e) {
            log.error("Failed to retrieve access token", e);
            throw new RuntimeException("Failed to retrieve access token", e);
        }
    }

}

package com.networth.userservice.util;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.AdminAccessDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.feign.KeycloakAdminClient;
import feign.Feign;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HelperUtils {

    private final KeycloakProperties keycloakProperties;
    public HelperUtils(KeycloakProperties keycloakProperties) {
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

        KeycloakAdminClient keycloakAdminClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(KeycloakAdminClient.class, keycloakProperties.getBaseUri());

        // Create the request form data
        AdminAccessDto formData = new AdminAccessDto();
        formData.setClient_id(keycloakProperties.getKeyAdmin().getClientId());
        formData.setGrant_type("password");
        formData.setUsername(keycloakProperties.getKeyAdmin().getUsername());
        formData.setPassword(keycloakProperties.getKeyAdmin().getPassword());

        log.info(String.valueOf(formData));

        try {
            TokenResponse tokenResponse = keycloakAdminClient.getAdminAccessToken(formData);

            log.info("Extracted access token");
            return tokenResponse.getAccessToken();
        } catch (Exception e) {
            log.error("Failed to retrieve access token", e);
            throw new RuntimeException("Failed to retrieve access token", e);
        }
    }


}

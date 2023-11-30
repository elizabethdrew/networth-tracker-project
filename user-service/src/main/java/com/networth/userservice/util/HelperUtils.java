package com.networth.userservice.util;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.feign.KeycloakClient;
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
import org.springframework.stereotype.Service;

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
    public Boolean validatePassword(String password) {

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

        return result.isValid();

    }

    // Get Keycloak Admin Access Token
    public String getAdminAccessToken() {
        log.info("Keycloak Admin Access Flow Started");

        KeycloakClient keycloakClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(KeycloakClient.class, keycloakProperties.getBaseUri());

        // Create the request form data
        KeycloakAccessDto formData = new KeycloakAccessDto();
        formData.setClientId(keycloakProperties.getKeyAdmin().getClientId());
        formData.setGrantType("password");
        formData.setUsername(keycloakProperties.getKeyAdmin().getUsername());
        formData.setPassword(keycloakProperties.getKeyAdmin().getPassword());

        log.info(String.valueOf(formData));

        try {
            TokenResponse tokenResponse = keycloakClient.getAdminAccessToken(formData);
            log.info("Extracted access token");
            return tokenResponse.getAccessToken();
        } catch (Exception e) {
            log.error("Failed to retrieve access token", e);
            throw new RuntimeException("Failed to retrieve access token", e);
        }
    }

    // Get Keycloak User Access Token
    public TokenResponse getUserAccessToken(LoginDto loginDto) {
        log.info("Keycloak User Access Flow Started");

        KeycloakClient keycloakClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(KeycloakClient.class, keycloakProperties.getBaseUri());

        // Create the request form data
        KeycloakAccessDto formData = new KeycloakAccessDto();
        formData.setClientId(keycloakProperties.getKeyUser().getClientId());
        formData.setClientSecret(keycloakProperties.getKeyUser().getClientSecret());
        formData.setScope("openid email profile");
        formData.setGrantType("password");
        formData.setUsername(loginDto.getUsername());
        formData.setPassword(loginDto.getPassword());

        log.info(String.valueOf(formData));

        try {
            TokenResponse tokenResponse = keycloakClient.getUserAccessToken(formData);
            log.info("Extracted access token");
            return tokenResponse;
        } catch (Exception e) {
            log.error("Failed to retrieve access token", e);
            throw new RuntimeException("Failed to retrieve access token", e);
        }
    }

}

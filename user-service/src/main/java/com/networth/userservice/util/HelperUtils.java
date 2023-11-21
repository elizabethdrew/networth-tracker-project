package com.networth.userservice.util;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HelperUtils {

    private final WebClient webClient;
    private final KeycloakProperties keycloakProperties;

    public HelperUtils(WebClient webClient, KeycloakProperties keycloakProperties) {
        this.webClient = webClient;
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
    public Mono<String> getAdminAccessToken() {

        log.info("Keycloak Admin Access Flow Started");

        return webClient.post()
                .uri(keycloakProperties.getBaseUri() + "/realms/" + keycloakProperties.getAdmin().getRealm() + "/protocol/openid-connect/token")
                .body(BodyInserters.fromFormData("client_id", keycloakProperties.getAdmin().getClientId())
                        .with("grant_type", "password")
                        .with("username", keycloakProperties.getAdmin().getUsername())
                        .with("password", keycloakProperties.getAdmin().getPassword()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("Error response from Keycloak: Status Code: {}, Body: {}", clientResponse.statusCode(), errorBody);
                                return Mono.error(new KeycloakException("Error response from Keycloak: Status Code: " +
                                        clientResponse.statusCode() + ", Body: " + errorBody));
                            });
                })
                .bodyToMono(JsonNode.class)
                .doOnNext(jsonNode -> log.info("Received access token from Keycloak"))
                .map(jsonNode -> {
                    String accessToken = jsonNode.get("access_token").asText();
                    log.info("Extracted access token");
                    return accessToken;
                })
                .doOnError(e -> log.error("Failed to retrieve access token", e));
    }
}

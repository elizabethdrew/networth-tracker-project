package com.drew.gatewayserver.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.resource-server.opaque-token")
@Data
@Validated
public class KeycloakProperties {

    @NotEmpty
    private String introspectionUri;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;

}

package com.networth.userservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String baseUri;
    private Admin admin = new Admin();

    @Getter
    @Setter
    public static class Admin {
        private String realm;
        private String clientId;
        private String username;
        private String password;
    }
}

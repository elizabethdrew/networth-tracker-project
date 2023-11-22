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
    private String logoutRedirectUrl;
    private KeyAdmin keyAdmin = new KeyAdmin();
    private KeyUser keyUser = new KeyUser();

    @Getter
    @Setter
    public static class KeyAdmin {
        private String realm;
        private String clientId;
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class KeyUser {
        private String realm;
        private String clientId;
        private String clientSecret;
    }


}

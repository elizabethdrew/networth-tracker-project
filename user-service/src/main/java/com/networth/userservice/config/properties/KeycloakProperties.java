package com.networth.userservice.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Data
@Validated
public class KeycloakProperties {


    @NotEmpty
    private String baseUri;
    @NotEmpty
    private String logoutRedirectUrl;

    @Valid
    private KeyAdmin keyAdmin = new KeyAdmin();

    @Valid
    private KeyUser keyUser = new KeyUser();

    @Data
    public static class KeyAdmin {

        @NotEmpty
        private String realm;
        @NotEmpty
        private String clientId;
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Data
    public static class KeyUser {

        @NotEmpty
        private String realm;
        @NotEmpty
        private String clientId;
        @NotEmpty
        private String clientSecret;
    }

}

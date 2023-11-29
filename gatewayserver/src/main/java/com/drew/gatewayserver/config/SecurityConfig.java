package com.drew.gatewayserver.config;

import com.drew.gatewayserver.filters.KeycloakTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    private final KeycloakProperties keycloakProperties;

    public SecurityConfig(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/users").permitAll() // Allow User Registration
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll() // Allow User Login
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(opaqueToken -> opaqueToken
                                .introspectionUri(keycloakProperties.getIntrospectionUri())
                                .introspectionClientCredentials(keycloakProperties.getClientId(), keycloakProperties.getClientSecret())
                        )
                )
        .csrf(csrf -> csrf.disable());
        return http.build();
    }

}

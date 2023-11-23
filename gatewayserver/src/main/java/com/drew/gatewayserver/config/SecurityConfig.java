package com.drew.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

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
                                .introspectionUri("http://keycloak:8080/realms/networth/protocol/openid-connect/token/introspect")
                                .introspectionClientCredentials("apigateway", "Ow1dayvip5w4BHJeacRzVLHLCfJNCm3W")
                        )
                )
        .csrf(csrf -> csrf.disable());
        return http.build();
    }

}

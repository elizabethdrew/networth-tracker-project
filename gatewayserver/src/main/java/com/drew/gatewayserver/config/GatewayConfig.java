package com.drew.gatewayserver.config;

import com.drew.gatewayserver.filters.KeycloakTokenFilter;
import com.drew.gatewayserver.filters.ResponseHeaderGlobalFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public GlobalFilter responseHeaderFilter() {
        return new ResponseHeaderGlobalFilter();
    }

    @Bean
    public GlobalFilter keycloakTokenFilter() {
        return new KeycloakTokenFilter();
    }
}
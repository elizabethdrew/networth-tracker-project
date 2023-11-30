package com.drew.gatewayserver.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class KeycloakTokenFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Starting token filter");
        List<String> authHeaders = exchange.getRequest().getHeaders().get("Authorization");

        if (authHeaders == null || authHeaders.isEmpty()) {
            return chain.filter(exchange);
        }

        String authHeader = authHeaders.get(0);
        if (!authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        String uid = extractUidFromToken(token);
        if (uid == null) {
            return chain.filter(exchange);
        }

        log.info("Added X-User-ID header: " + uid);
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-ID", uid)
                .build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private String extractUidFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            String payload = parts[1];
            String decodedJson = new String(Base64Utils.decodeFromString(payload));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claims = objectMapper.readValue(decodedJson, Map.class);
            return claims.get("sub").toString();
        } catch (IOException e) {
            log.error("Can't Extract Uid From Token", e);
            return null;
        }
    }
}


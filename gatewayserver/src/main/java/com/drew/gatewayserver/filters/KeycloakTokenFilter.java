package com.drew.gatewayserver.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
        log.info("starting token filter");
        List<String> authHeaders = exchange.getRequest().getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String uid = extractUidFromToken(token);
                log.info("Uid: " + uid);
                if (uid != null) {
                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        exchange.getResponse().getHeaders().set("X-User-ID", uid);
                    }));
                }
            }
        }
        return chain.filter(exchange);
    }

    private String extractUidFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {return null;}
            String payload = parts[1]; // Get the payload part
            String decodedJson = new String(Base64Utils.decodeFromString(payload));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claims = objectMapper.readValue(decodedJson, Map.class);
            String sub = claims.get("sub").toString();
            return sub;
        } catch (IOException e) {
            log.error("Can't Extract Uid From Token");
            return null;
        }
    }
}


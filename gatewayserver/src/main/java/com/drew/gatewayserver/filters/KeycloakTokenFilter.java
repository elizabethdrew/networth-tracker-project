package com.drew.gatewayserver.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class KeycloakTokenFilter extends AbstractGatewayFilterFactory<KeycloakTokenFilter.Config> {

    public KeycloakTokenFilter() {
        super(Config.class);
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String idToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (idToken != null && idToken.startsWith("Bearer ")) {
                idToken = idToken.substring(7); // Remove "Bearer " prefix
                Claims claims = Jwts.parserBuilder()
                        .build()
                        .parseClaimsJws(idToken)
                        .getBody();

                String sid = claims.get("sid", String.class);

                exchange.getRequest().mutate()
                        .header("X-SID", sid)
                        .build();
            }

            return chain.filter(exchange);
        };
    }
}

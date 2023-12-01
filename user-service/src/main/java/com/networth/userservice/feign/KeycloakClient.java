package com.networth.userservice.feign;

import com.networth.userservice.dto.UpdateKeycloakDto;
import com.networth.userservice.dto.UserRepresentationDto;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "keycloak", url = "${keycloak.base-uri}", configuration = JacksonFeignClientConfig.class, fallback = KeycloakFallback.class)
public interface KeycloakClient {

    @PostMapping(value = "/admin/realms/networth/users", consumes = "application/json")
    Response createKeycloakUser(@RequestHeader Map<String, Object> headers,
                                @RequestBody UserRepresentationDto formData);

    @PutMapping(value = "/admin/realms/networth/users/{keycloakId}", consumes = "application/json")
    Response updateKeycloakUser(@RequestHeader Map<String, Object> headers,
                                @PathVariable("keycloakId") String keycloakId,
                                @RequestBody UpdateKeycloakDto formData);
}

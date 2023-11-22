package com.networth.userservice.feign;

import com.networth.userservice.dto.PasswordRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "keycloak", url = "${keycloak.base-uri}")
public interface KeycloakClient {

    @PutMapping("/admin/realms/{realm}/users/{id}/reset-password")
    void resetUserPassword(
            @PathVariable("realm") String realm,
            @PathVariable("id") String keycloakId,
            @RequestBody PasswordRepresentation passwordRepresentation,
            @RequestHeader("Authorization") String bearerToken);

}

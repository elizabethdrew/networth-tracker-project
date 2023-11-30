package com.networth.userservice.feign;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.TokenResponse;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "keycloak2", url = "${keycloak.base-uri}", configuration = FormFeignClientConfig.class, fallback = KeycloakFormFallback.class)
public interface KeycloakFormClient {

    @PostMapping(value = "/realms/master/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponse getAdminAccessToken(KeycloakAccessDto formData);

    @PostMapping(value = "/realms/networth/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponse getUserAccessToken(KeycloakAccessDto formData);

    @PostMapping(value = "/realms/networth/protocol/openid-connect/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Response keycloakLogout(KeycloakAccessDto formData);

    @PostMapping(value = "/realms/networth/protocol/openid-connect/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Response keycloakRevoke(KeycloakAccessDto formData);

}

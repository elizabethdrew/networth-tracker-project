package com.networth.userservice.feign;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.dto.UpdateUserEmailDto;
import com.networth.userservice.dto.UserRepresentationDto;
import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name="keycloak", fallback = KeycloakFallback.class)
public interface KeycloakClient {

    @RequestLine("POST /admin/realms/networth/users")
    @Headers("Content-Type: application/json")
    Response createKeycloakUser(@HeaderMap Map<String, Object> headers,
                                @RequestBody UserRepresentationDto formData);

    @RequestLine("POST /admin/realms/networth/users/{keycloakId}")
    @Headers("Content-Type: application/json")
    Response updateKeycloakUser(@HeaderMap Map<String, Object> headers,
                                @Param String keycloakId,
                                @RequestBody UpdateUserEmailDto formData);

    @RequestLine("POST /realms/master/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getAdminAccessToken(@RequestBody KeycloakAccessDto formData);

    @RequestLine("POST /realms/networth/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getUserAccessToken(@RequestBody KeycloakAccessDto formData);

    @RequestLine("POST /realms/networth/protocol/openid-connect/logout")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Response keycloakLogout(@RequestBody KeycloakAccessDto formData);

    @RequestLine("POST /realms/networth/protocol/openid-connect/revoke")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Response keycloakRevoke(@RequestBody KeycloakAccessDto formData);

}

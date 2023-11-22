package com.networth.userservice.feign;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.dto.TokenResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface KeycloakClient {

    @RequestLine("POST /realms/master/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getAdminAccessToken(@RequestBody KeycloakAccessDto formData);

    @RequestLine("POST /realms/networth/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getUserAccessToken(@RequestBody KeycloakAccessDto formData);

    @RequestLine("POST /realms/networth/protocol/openid-connect/logout")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Response keycloakLogout(@RequestBody KeycloakAccessDto formData);

}

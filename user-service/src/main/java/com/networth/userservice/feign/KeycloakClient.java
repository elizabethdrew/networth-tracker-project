package com.networth.userservice.feign;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.PasswordRepresentation;
import com.networth.userservice.dto.TokenResponse;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

public interface KeycloakClient {

    @RequestLine("POST /realms/master/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getAdminAccessToken(@RequestBody KeycloakAccessDto formData);

    @RequestLine("POST /realms/networth/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getUserAccessToken(@RequestBody KeycloakAccessDto formData);

    @RequestLine("POST /admin/realms/{realm}/users/{id}/reset-password")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    void updateUserPassword(@RequestBody PasswordRepresentation passwordRepresentation);

}

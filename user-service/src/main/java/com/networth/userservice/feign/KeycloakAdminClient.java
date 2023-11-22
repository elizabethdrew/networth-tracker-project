package com.networth.userservice.feign;

import com.networth.userservice.dto.AdminAccessDto;
import com.networth.userservice.dto.TokenResponse;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;


public interface KeycloakAdminClient {
    @RequestLine("POST /realms/master/protocol/openid-connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getAdminAccessToken(@RequestBody AdminAccessDto formData);
}

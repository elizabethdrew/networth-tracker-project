package com.networth.userservice.feign;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.TokenResponse;
import feign.Response;

public class KeycloakFormFallback implements KeycloakFormClient {
    @Override
    public TokenResponse getAdminAccessToken(KeycloakAccessDto formData) {
        return null;
    }

    @Override
    public TokenResponse getUserAccessToken(KeycloakAccessDto formData) {
        return null;
    }

    @Override
    public Response keycloakLogout(KeycloakAccessDto formData) {
        return null;
    }

    @Override
    public Response keycloakRevoke(KeycloakAccessDto formData) {
        return null;
    }
}

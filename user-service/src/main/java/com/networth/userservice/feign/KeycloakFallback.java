package com.networth.userservice.feign;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.dto.UpdateUserEmailDto;
import com.networth.userservice.dto.UserRepresentationDto;
import feign.Response;

import java.util.Map;

public class KeycloakFallback implements KeycloakClient {
    @Override
    public Response createKeycloakUser(Map<String, Object> headers, UserRepresentationDto formData) {
        return null;
    }

    @Override
    public Response updateKeycloakUser(Map<String, Object> headers, String keycloakId, UpdateUserEmailDto formData) {
        return null;
    }

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

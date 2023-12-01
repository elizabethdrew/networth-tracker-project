package com.networth.userservice.feign;

import com.networth.userservice.dto.UpdateKeycloakDto;
import com.networth.userservice.dto.UserRepresentationDto;
import feign.Response;

import java.util.Map;

public class KeycloakFallback implements KeycloakClient {
    @Override
    public Response createKeycloakUser(Map<String, Object> headers, UserRepresentationDto formData) {
        return null;
    }

    @Override
    public Response updateKeycloakUser(Map<String, Object> headers, String keycloakId, UpdateKeycloakDto formData) {
        return null;
    }
}

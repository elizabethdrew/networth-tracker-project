package com.networth.userservice.service;

import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.TokenResponse;
import com.networth.userservice.dto.UpdateKeycloakDto;
import com.networth.userservice.dto.UserRepresentationDto;
import com.networth.userservice.exception.AuthenticationServiceException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserServiceException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.feign.KeycloakFormClient;
import com.networth.userservice.util.KeycloakFormDataBuilder;
import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class KeycloakService {

    private final KeycloakFormClient keycloakFormClient;
    private final KeycloakClient keycloakClient;
    private final KeycloakFormDataBuilder keycloakFormDataBuilder;

    public KeycloakService(KeycloakFormClient keycloakFormClient, KeycloakClient keycloakClient, KeycloakFormDataBuilder keycloakFormDataBuilder) {
        this.keycloakFormClient = keycloakFormClient;
        this.keycloakClient = keycloakClient;
        this.keycloakFormDataBuilder = keycloakFormDataBuilder;
    }

    // Get Keycloak Admin Access Token
    public String getAdminAccessToken() {
        try {
            log.info("Keycloak Admin Access Flow Started");
            KeycloakAccessDto formData = keycloakFormDataBuilder.buildAdminAccessFormData();
            TokenResponse tokenResponse = keycloakFormClient.getAdminAccessToken(formData);
            log.info("Extracted access token");
            return tokenResponse.getAccessToken();
        } catch (FeignException fe) {
            log.error("Feign exception while retrieving admin access token", fe);
            throw new AuthenticationServiceException("Feign error during admin access token retrieval", fe);
        } catch (Exception e) {
            log.error("Failed to retrieve admin access token", e);
            throw new AuthenticationServiceException("Error during admin access token retrieval", e);
        }
    }

    // Get Keycloak User Access Token
    public TokenResponse getUserAccessToken(LoginDto loginDto) {
        try {
            log.info("Keycloak User Access Flow Started");
            KeycloakAccessDto formData = keycloakFormDataBuilder.buildUserAccessFormData(loginDto);
            TokenResponse tokenResponse = keycloakFormClient.getUserAccessToken(formData);
            log.info("Extracted access token");
            return tokenResponse;
        } catch (FeignException fe) {
            log.error("Feign exception while retrieving user access token", fe);
            throw new AuthenticationServiceException("Feign error during user access token retrieval", fe);
        } catch (Exception e) {
            log.error("Failed to retrieve user access token", e);
            throw new AuthenticationServiceException("Error during user access token retrieval", e);
        }
    }

    public Response createUser(RegisterDto registerDto) {
        try {
            log.info("Registering user '{}' with Keycloak.", registerDto.getUsername());
            Map<String, Object> headers = createHeadersWithAdminAccessToken();
            UserRepresentationDto formData = keycloakFormDataBuilder.createUserRepresentation(registerDto);

            log.info("Adding User To Keycloak");
            Response response = keycloakClient.createKeycloakUser(headers, formData);
            handleKeycloakResponse(response, HttpStatus.CREATED, "Failed to create user in Keycloak");
            return response;
        } catch(FeignException e) {
            log.error("Keycloak create user endpoint responded with an error", e);
            throw new AuthenticationServiceException("Failed to create user in Keycloak", e);
        } catch(Exception e) {
            log.error("An unexpected error occurred during user creation", e);
            throw new UserServiceException("Unexpected error during user creation", e);
        }
    }

    public void logoutUser(LogoutDto logoutDto) {
        try {
            log.debug("Logging out user from Keycloak");
            Response response = keycloakFormClient.keycloakLogout(keycloakFormDataBuilder.buildLogoutData(logoutDto));
            handleKeycloakResponse(response, HttpStatus.NO_CONTENT, "User logout failed");
            log.info("User logged out successfully");
        } catch (FeignException e) {
            log.error("Keycloak logout endpoint responded with an error", e);
            throw new AuthenticationServiceException("Failed to logout user from Keycloak", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during user logout", e);
            throw new UserServiceException("Unexpected error during user logout", e);
        }
    }

    public void updateEmailKeycloak(String email, String keycloakId) {
        try {
            log.info("Starting Update User in Keycloak");
            Map<String, Object> headers = createHeadersWithAdminAccessToken();
            UpdateKeycloakDto formData = new UpdateKeycloakDto();
            formData.setEmail(email);

            Response response = keycloakClient.updateKeycloakUser(headers, keycloakId, formData);
            handleKeycloakResponse(response, HttpStatus.NO_CONTENT, "Failed to update user in Keycloak");
            log.info("Email updated in Keycloak successfully");
        } catch (FeignException e) {
            log.error("Error communicating with Keycloak: ", e);
            throw new AuthenticationServiceException("Error communicating with Keycloak", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during email update in Keycloak", e);
            throw new UserServiceException("Unexpected error during email update in Keycloak", e);
        }
    }



    public Optional<String> extractKeycloakUserId(Response response) {
        log.debug("Extracting Keycloak User Id from response");

        if (response.status() == HttpStatus.CREATED.value()) {
            return response.headers().get("Location").stream()
                    .findFirst()
                    .map(URI::create)
                    .map(URI::getPath)
                    .map(path -> path.substring(path.lastIndexOf('/') + 1))
                    .filter(keycloakUserId -> !keycloakUserId.isEmpty());
        } else {
            return Optional.empty();
        }
    }

    public void revokeAccessToken(String accessToken) {
        try {
            log.debug("Revoking access token for user");
            Response response = keycloakFormClient.keycloakRevoke(keycloakFormDataBuilder.buildRevokeData(accessToken));
            handleKeycloakResponse(response, HttpStatus.NO_CONTENT, "Access token revocation failed");
            log.info("Access token revoked successfully");
        } catch (FeignException e) {
            log.error("Keycloak revocation endpoint responded with an error", e);
            throw new AuthenticationServiceException("Failed to revoke access token in Keycloak", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during access token revocation", e);
            throw new UserServiceException("Unexpected error during access token revocation", e);
        }
    }

    private void handleKeycloakResponse(Response response, HttpStatus expectedStatus, String errorMessage) {
        if (response.status() != expectedStatus.value()) {
            log.error(errorMessage + ". Status: {}, Body: {}", response.status(), response.body());
            throw new KeycloakException(errorMessage + ". Status: " + response.status());
        }
    }

    private Map<String, Object> createHeadersWithAdminAccessToken() {
        String accessToken = getAdminAccessToken();
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        return headers;
    }

}

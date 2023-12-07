package com.networth.userservice.util;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.dto.PasswordCredentialDto;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserRepresentationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class KeycloakFormDataBuilder {

    private final KeycloakProperties keycloakProperties;

    public KeycloakFormDataBuilder(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    public KeycloakAccessDto buildUserAccessFormData(LoginDto loginDto) {
        KeycloakAccessDto formData = new KeycloakAccessDto();
        formData.setClientId(keycloakProperties.getKeyUser().getClientId());
        formData.setClientSecret(keycloakProperties.getKeyUser().getClientSecret());
        formData.setScope("openid email profile");
        formData.setGrantType("password");
        formData.setUsername(loginDto.getUsername());
        formData.setPassword(loginDto.getPassword());
        return formData;
    }

    public KeycloakAccessDto buildAdminAccessFormData() {
        KeycloakAccessDto formData = new KeycloakAccessDto();
        formData.setClientId(keycloakProperties.getKeyAdmin().getClientId());
        formData.setGrantType("password");
        formData.setUsername(keycloakProperties.getKeyAdmin().getUsername());
        formData.setPassword(keycloakProperties.getKeyAdmin().getPassword());
        return formData;
    }

    public UserRepresentationDto createUserRepresentation(RegisterDto registerDto) {
        log.debug("Constructing UserRepresentationDto for username: {}", registerDto.getUsername());

        UserRepresentationDto formData = new UserRepresentationDto();
        formData.setUsername(registerDto.getUsername());
        formData.setEmail(registerDto.getEmail());
        formData.setEnabled(true);

        PasswordCredentialDto password = new PasswordCredentialDto();
        password.setType("PASSWORD");
        password.setValue(registerDto.getPassword());
        password.setTemporary(false);

        formData.setCredentials(Collections.singletonList(password));

        log.debug("UserRepresentationDto constructed for username: {}", registerDto.getUsername());
        return formData;
    }

    public KeycloakAccessDto buildRevokeData(String accessToken) {
        KeycloakAccessDto revokeData = new KeycloakAccessDto();
        revokeData.setClientId(keycloakProperties.getKeyUser().getClientId());
        revokeData.setClientSecret(keycloakProperties.getKeyUser().getClientSecret());
        revokeData.setToken(accessToken);
        return revokeData;
    }

    public KeycloakAccessDto buildLogoutData(LogoutDto logoutDto) {
        KeycloakAccessDto formData = new KeycloakAccessDto();
        formData.setClientId(keycloakProperties.getKeyUser().getClientId());
        formData.setClientSecret(keycloakProperties.getKeyUser().getClientSecret());
        formData.setRefreshToken(logoutDto.getRefreshToken());
        formData.setIdTokenHint(logoutDto.getIdTokenHint());
        formData.setPostLogoutRedirectUri(keycloakProperties.getLogoutRedirectUrl());
        return formData;
    }
}

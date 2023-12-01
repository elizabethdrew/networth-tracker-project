package com.networth.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserRepresentationDto {

    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("credentials")
    private List<PasswordCredentialDto> credentials;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<PasswordCredentialDto> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<PasswordCredentialDto> credentials) {
        this.credentials = credentials;
    }
}

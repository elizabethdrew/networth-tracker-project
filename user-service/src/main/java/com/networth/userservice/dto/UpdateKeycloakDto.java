package com.networth.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateKeycloakDto {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("email")
    private String email;

}

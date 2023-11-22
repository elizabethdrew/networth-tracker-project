package com.networth.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserRepresentationDto {

    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("credentials")
    private List<PasswordCredentialDto> credentials;
}

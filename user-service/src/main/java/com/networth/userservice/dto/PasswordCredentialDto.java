package com.networth.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PasswordCredentialDto {
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private String value;
    @JsonProperty("temporary")
    private Boolean temporary;
}

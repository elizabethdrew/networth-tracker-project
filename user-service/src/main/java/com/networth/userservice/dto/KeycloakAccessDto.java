package com.networth.userservice.dto;

import feign.form.FormProperty;
import lombok.Data;

@Data
public class KeycloakAccessDto {

    @FormProperty("client_id")
    private String client_id;
    @FormProperty("client_secret")
    private String client_secret;
    @FormProperty("scope")
    private String scope;
    @FormProperty("grant_type")
    private String grant_type;
    @FormProperty("username")
    private String username;
    @FormProperty("password")
    private String password;

}

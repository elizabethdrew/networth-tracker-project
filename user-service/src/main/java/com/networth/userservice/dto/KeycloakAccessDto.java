package com.networth.userservice.dto;

import feign.form.FormProperty;
import lombok.Data;

@Data
public class KeycloakAccessDto {

    @FormProperty("client_id")
    private String client_id;
    @FormProperty("client_secret")
    private String client_secret;
    @FormProperty("token")
    private String token;
    @FormProperty("refresh_token")
    private String refresh_token;
    @FormProperty("id_token_hint")
    private String id_token_hint;
    @FormProperty("post_logout_redirect_uri")
    private String post_logout_redirect_uri;
    @FormProperty("scope")
    private String scope;
    @FormProperty("grant_type")
    private String grant_type;
    @FormProperty("username")
    private String username;
    @FormProperty("password")
    private String password;

}

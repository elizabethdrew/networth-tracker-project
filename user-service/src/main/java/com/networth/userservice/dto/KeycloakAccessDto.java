package com.networth.userservice.dto;

import feign.form.FormProperty;

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

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getId_token_hint() {
        return id_token_hint;
    }

    public void setId_token_hint(String id_token_hint) {
        this.id_token_hint = id_token_hint;
    }

    public String getPost_logout_redirect_uri() {
        return post_logout_redirect_uri;
    }

    public void setPost_logout_redirect_uri(String post_logout_redirect_uri) {
        this.post_logout_redirect_uri = post_logout_redirect_uri;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

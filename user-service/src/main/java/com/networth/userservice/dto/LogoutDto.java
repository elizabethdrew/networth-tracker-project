package com.networth.userservice.dto;

public class LogoutDto {
    private String access_token;
    private String refresh_token;
    private String id_token_hint;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
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
}

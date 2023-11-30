package com.networth.userservice.dto;

public class LogoutDto {
    private String accessToken;
    private String refreshToken;
    private String idTokenHint;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIdTokenHint() {
        return idTokenHint;
    }

    public void setIdTokenHint(String idTokenHint) {
        this.idTokenHint = idTokenHint;
    }
}

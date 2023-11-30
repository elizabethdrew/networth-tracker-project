package com.networth.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse extends TokenResponse {

    @JsonProperty("user_id")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

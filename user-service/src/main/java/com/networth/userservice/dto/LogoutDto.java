package com.networth.userservice.dto;

import feign.form.FormProperty;
import lombok.Data;

@Data
public class LogoutDto {
    @FormProperty("refresh_token")
    private String refreshToken;
    @FormProperty("id_token_hint")
    private String idTokenHint;
}

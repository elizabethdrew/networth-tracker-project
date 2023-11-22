package com.networth.userservice.dto;

import feign.form.FormProperty;
import lombok.Data;

@Data
public class LogoutDto {
    private String refresh_token;
    private String id_token_hint;
}

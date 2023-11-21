package com.networth.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends TokenResponse {

    private Long userId;

}

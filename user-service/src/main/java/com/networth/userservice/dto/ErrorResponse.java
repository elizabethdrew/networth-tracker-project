package com.networth.userservice.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer code;
    private String message;
}

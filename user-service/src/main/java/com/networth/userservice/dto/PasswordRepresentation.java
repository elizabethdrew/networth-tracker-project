package com.networth.userservice.dto;

import lombok.Data;

@Data
public class PasswordRepresentation {
    private String type;
    private String value;
    private Boolean temporary;
}

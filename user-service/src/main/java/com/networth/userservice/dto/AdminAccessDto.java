package com.networth.userservice.dto;

import feign.form.FormProperty;
import lombok.Data;

@Data
public class AdminAccessDto {

    @FormProperty("client_id")
    private String client_id;
    @FormProperty("grant_type")
    private String grant_type;
    @FormProperty("username")
    private String username;
    @FormProperty("password")
    private String password;

}

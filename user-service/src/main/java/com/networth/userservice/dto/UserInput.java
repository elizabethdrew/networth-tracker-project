package com.networth.userservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInput {

    private String Username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private TaxRate taxRate;

}

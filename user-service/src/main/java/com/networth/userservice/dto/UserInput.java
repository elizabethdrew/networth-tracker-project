package com.networth.userservice.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInput {

    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private TaxRate taxRate;

}

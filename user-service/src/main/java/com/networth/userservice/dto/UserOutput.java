package com.networth.userservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserOutput {

    private Long Id;
    private String Username;
    private String email;
    private LocalDate dateOfBirth;
    private TaxRate taxRate;
    private LocalDate dateOpened;
    private LocalDate dateUpdated;
    private Boolean activeUser;

}

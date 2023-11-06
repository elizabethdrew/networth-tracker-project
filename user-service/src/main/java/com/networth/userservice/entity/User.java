package com.networth.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "user_id")
    private Long userId;

    @Column (name = "username", unique = true)
    private String Username;

    @Column( name = "email", unique = true)
    private String email;

    @Column( name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column ( name = "user_status")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column ( name = "tax_rate")
    private TaxRate taxRate;

    @Column ( name = "date_birth")
    private LocalDate dateOfBirth;

    @Column ( name = "date_open")
    private LocalDate dateOpened;

    @Column (name = "date_updated")
    private LocalDate dateUpdated;

    enum TaxRate {
        BASIC,
        HIGHER,
        ADDITIONAL
    };

    enum UserStatus {
        ACTIVE,
        CLOSED
    }


}
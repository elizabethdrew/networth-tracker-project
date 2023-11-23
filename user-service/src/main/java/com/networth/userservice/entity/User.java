package com.networth.userservice.entity;

import com.networth.userservice.dto.TaxRate;
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
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "user_id")
    private Long userId;

    @Column (name = "keycloak_id", unique = true)
    private String keycloakId;

    @Column (name = "username", unique = true)
    private String username;

    @Column( name = "email", unique = true)
    private String email;

    @Column ( name = "active_user")
    private Boolean activeUser = true;

    @Enumerated(EnumType.STRING)
    @Column ( name = "tax_rate")
    private TaxRate taxRate;

    @Column ( name = "date_birth")
    private LocalDate dateOfBirth;

    @Column ( name = "date_open")
    private LocalDateTime dateOpened;

    @Column (name = "date_updated")
    private LocalDateTime dateUpdated;

}
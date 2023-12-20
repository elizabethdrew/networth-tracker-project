package com.drew.accountservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.drew.commonlibrary.types.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "keycloak_id", nullable = false)
    private String keycloakId;

    @Column(name = "account_nickname", nullable = false)
    private String accountNickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AccountType type;

    @Column(name = "currency")
    private String currency;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "percentage_ownership")
    private Long percentageOwnership;

    @Column(name = "fixed_term")
    private Boolean fixedTerm = false;

    @Column(name = "fixed_term_end_date")
    private LocalDateTime fixedTermEndDate;

    @Column(name = "date_opened")
    private LocalDateTime dateOpened;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "notes")
    private String notes;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getAccountNickname() {
        return accountNickname;
    }

    public void setAccountNickname(String accountNickname) {
        this.accountNickname = accountNickname;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Long getPercentageOwnership() {
        return percentageOwnership;
    }

    public void setPercentageOwnership(Long percentageOwnership) {
        this.percentageOwnership = percentageOwnership;
    }

    public Boolean getFixedTerm() {
        return fixedTerm;
    }

    public void setFixedTerm(Boolean fixedTerm) {
        this.fixedTerm = fixedTerm;
    }

    public LocalDateTime getFixedTermEndDate() {
        return fixedTermEndDate;
    }

    public void setFixedTermEndDate(LocalDateTime fixedTermEndDate) {
        this.fixedTermEndDate = fixedTermEndDate;
    }

    public LocalDateTime getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDateTime dateOpened) {
        this.dateOpened = dateOpened;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public enum AccountStatus {
        IN_USE,
        ARCHIVED,
        DORMANT
    }
}

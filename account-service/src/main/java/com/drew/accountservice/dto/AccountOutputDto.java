package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountOutputDto {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("account_nickname")
    private String accountNickname;

    @JsonProperty("type")
    private Account.AccountType type;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("credit_limit")
    private BigDecimal creditLimit;

    @JsonProperty("percentage_ownership")
    private Long percentageOwnership;

    @JsonProperty("fixed_term")
    private Boolean fixedTerm;

    @JsonProperty("fixed_term_end_date")
    private LocalDateTime fixedTermEndDate;

    @JsonProperty("date_opened")
    private LocalDateTime dateOpened;

    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    @JsonProperty("date_updated")
    private LocalDateTime dateUpdated;

    @JsonProperty("status")
    private Account.AccountStatus status;

    @JsonProperty("notes")
    private String notes;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNickname() {
        return accountNickname;
    }

    public void setAccountNickname(String accountNickname) {
        this.accountNickname = accountNickname;
    }

    public Account.AccountType getType() {
        return type;
    }

    public void setType(Account.AccountType type) {
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

    public Account.AccountStatus getStatus() {
        return status;
    }

    public void setStatus(Account.AccountStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}

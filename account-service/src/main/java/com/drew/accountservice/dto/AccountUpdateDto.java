package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountUpdateDto {

    @JsonProperty("account_nickname")
    private String accountNickname;

    @JsonProperty("credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @JsonProperty("date_opened")
    private LocalDateTime dateOpened;

    @JsonProperty("status")
    private Account.AccountStatus status;

    @JsonProperty("notes")
    private String notes;

    public String getAccountNickname() {
        return accountNickname;
    }

    public void setAccountNickname(String accountNickname) {
        this.accountNickname = accountNickname;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public LocalDateTime getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDateTime dateOpened) {
        this.dateOpened = dateOpened;
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

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
}

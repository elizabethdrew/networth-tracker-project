package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Account;
import com.drew.commonlibrary.types.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountOutputDto(
    Long accountId,
    String accountNickname,
    AccountType type,
    String currency,
    BigDecimal creditLimit,
    BigDecimal currentBalance,
    Long percentageOwnership,
    Boolean fixedTerm,
    LocalDateTime fixedTermEndDate,
    LocalDateTime dateOpened,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated,
    Account.AccountStatus status,
    String notes

) {

}

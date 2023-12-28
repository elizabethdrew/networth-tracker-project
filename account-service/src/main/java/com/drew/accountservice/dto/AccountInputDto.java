package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Account;
import com.drew.commonlibrary.types.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountInputDto(
        String accountNickname,
        AccountType type,
        String currency,
        BigDecimal creditLimit,
        Long percentageOwnership,
        Boolean fixedTerm,
        LocalDateTime fixedTermEndDate,
        LocalDateTime dateOpened,
        Account.AccountStatus status,
        String notes

) {
}

package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountUpdateDto(
        String accountNickname,
        BigDecimal creditLimit,
        BigDecimal currentBalance,
        LocalDateTime dateOpened,
        Account.AccountStatus status,
        String notes
) {
}

package com.drew.accountservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.drew.commonlibrary.types.AccountType;

public record BalanceDto(
        Long balanceId,
        Long accountId,
        AccountType accountType,
        LocalDate reconcileDate,
        BigDecimal balance,
        BigDecimal differenceFromLast,
        BigDecimal depositValue,
        BigDecimal withdrawalValue,
        BigDecimal interestValue,
        BigDecimal feesValue,
        BigDecimal bonusValue,
        BigDecimal growthValue
) {
}

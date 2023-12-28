package com.drew.accountservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceDto(
        Long balanceId,
        Long accountId,
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

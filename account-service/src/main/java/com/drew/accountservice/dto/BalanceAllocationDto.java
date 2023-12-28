package com.drew.accountservice.dto;

import java.math.BigDecimal;

public record BalanceAllocationDto(
        BigDecimal balance,
        BigDecimal depositValue,
        BigDecimal withdrawalValue,
        BigDecimal interestValue,
        BigDecimal feesValue,
        BigDecimal bonusValue,
        BigDecimal growthValue
) {
}

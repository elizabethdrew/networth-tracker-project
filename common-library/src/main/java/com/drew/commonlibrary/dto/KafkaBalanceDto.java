package com.drew.commonlibrary.dto;

import java.math.BigDecimal;
import com.drew.commonlibrary.types.AccountType;

public record KafkaBalanceDto (
    Long accountId,
    String keycloakId,
    AccountType accountType,
    BigDecimal currentBalance,
    BigDecimal depositValue,
    BigDecimal withdrawalValue,
    BigDecimal interestValue,
    BigDecimal feesValue,
    BigDecimal bonusValue,
    BigDecimal growthValue ) {

}
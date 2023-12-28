package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Balance;

import java.util.List;

public record BalanceHistoryDto (
        List<Balance> balances
) {
}

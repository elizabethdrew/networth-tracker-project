package com.drew.accountservice.dto;

import java.util.List;

public record BalanceHistoryDto (
        List<BalanceDto> balanceHistory
) {
}

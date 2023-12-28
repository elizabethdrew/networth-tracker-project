package com.drew.accountservice.service;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.dto.BalanceDto;
import com.drew.accountservice.dto.BalanceHistoryDto;

public interface BalanceService {
    BalanceHistoryDto getBalanceHistory(String keycloakUserId, Long accountId);
    BalanceDto addNewBalance(Long accountId, String keycloakUserId, BalanceAllocationDto newAllocation);
    BalanceDto getBalanceById(String keycloakUserId, Long accountId, Long balanceId);
}




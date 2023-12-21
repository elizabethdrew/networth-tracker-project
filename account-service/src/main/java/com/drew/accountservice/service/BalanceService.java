package com.drew.accountservice.service;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.dto.BalanceHistoryDto;
import com.drew.accountservice.entity.Balance;

public interface BalanceService {
    BalanceHistoryDto getBalanceHistory(String keycloakUserId, Long accountId);
    Balance addNewBalance(Long accountId, String keycloakUserId, BalanceAllocationDto newAllocation);
}




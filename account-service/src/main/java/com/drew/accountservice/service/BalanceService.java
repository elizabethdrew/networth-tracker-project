package com.drew.accountservice.service;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.dto.BalanceHistoryDto;
import com.drew.commonlibrary.dto.KafkaBalanceDto;

public interface BalanceService {
    BalanceHistoryDto getBalanceHistory(String keycloakUserId, Long accountId)
    KafkaBalanceDto addNewBalance(Long accountId, String keycloakUserId, BalanceAllocationDto newAllocation);
}




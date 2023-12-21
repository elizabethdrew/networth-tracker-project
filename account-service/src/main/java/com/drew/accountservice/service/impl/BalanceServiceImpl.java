package com.drew.accountservice.service.impl;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.dto.BalanceHistoryDto;
import com.drew.accountservice.entity.Account;
import com.drew.accountservice.entity.Balance;
import com.drew.accountservice.exception.AccountNotFoundException;
import com.drew.accountservice.exception.InvalidAllocationException;
import com.drew.accountservice.kafka.KafkaService;
import com.drew.accountservice.mapper.BalanceMapper;
import com.drew.accountservice.repository.AccountRepository;
import com.drew.accountservice.repository.BalanceRepository;
import com.drew.accountservice.service.BalanceService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;
    private final AccountRepository accountRepository;
    private final KafkaService kafkaService;

    public BalanceServiceImpl(BalanceRepository balanceRepository, BalanceMapper balanceMapper, AccountRepository accountRepository, KafkaService kafkaService) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
        this.kafkaService = kafkaService;
        this.accountRepository = accountRepository;
    }

    @Override
    public BalanceHistoryDto getBalanceHistory(String keycloakUserId, Long accountId) {

        Account account = accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found or does not belong to user"));

        List<Balance> balanceHistory = balanceRepository.findByAccount(account);

        BalanceHistoryDto history = new BalanceHistoryDto();

        if (balanceHistory.isEmpty()) {
            history.setBalances(null);
            return history;
        }

        balanceHistory.sort((b1, b2) -> b2.getReconcileDate().compareTo(b1.getReconcileDate()));

        history.setBalances(balanceHistory);
        return history;
    }

    @Override
    @Transactional
    public Balance addNewBalance(Long accountId, String keycloakUserId, BalanceAllocationDto newAllocation) {

        Account account = accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found or does not belong to user"));

        BigDecimal lastBalance = account.getLatestBalance();

        BigDecimal difference = calculateDifference(lastBalance, newAllocation.getCurrentBalance());

        validateAllocations(difference, newAllocation);

        Balance newBalance = balanceMapper.toBalanceFromInput(newAllocation);
        newBalance.setDifferenceFromLast(difference);
        newBalance.setReconcileDate(LocalDate.now());
        newBalance.setAccount(account);
        Balance savedBalance = balanceRepository.save(newBalance);

        // If ISA account, tell ISA Service
        if (account.getType().toString().contains("ISA")) {
            log.info("New balance is ISA");
            kafkaService.newBalanceKafka("sendNewBalance-out-0", savedBalance, keycloakUserId);
        } else {
            log.info("New balance is not ISA");
        }

        return savedBalance;
    }

    private BigDecimal calculateDifference(BigDecimal lastBalance, BigDecimal currentBalance) {
        return Optional.ofNullable(currentBalance)
                .orElse(BigDecimal.ZERO)
                .subtract(Optional.ofNullable(lastBalance).orElse(BigDecimal.ZERO));
    }

    private void validateAllocations(BigDecimal difference, BalanceAllocationDto allocation) {
        BigDecimal totalAllocations = allocation.getDepositValue()
                .add(allocation.getInterestValue())
                .add(allocation.getBonusValue())
                .add(allocation.getGrowthValue())
                .subtract(allocation.getWithdrawalValue())
                .subtract(allocation.getFeesValue());

        if (difference.compareTo(totalAllocations) != 0) {
            throw new InvalidAllocationException("Allocation amounts do not match expected total value");
        }
    }
}


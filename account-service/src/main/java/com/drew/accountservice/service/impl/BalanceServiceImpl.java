package com.drew.accountservice.service.impl;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.dto.BalanceDto;
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
    @Transactional
    public BalanceDto addNewBalance(Long accountId, String keycloakUserId, BalanceAllocationDto newAllocation) {

        log.info("Allocation: " + newAllocation.toString());

        log.info("Account Id: " + String.valueOf(accountId));

        log.info("Keycloak Id: " + keycloakUserId);

        Account account = accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found or does not belong to user"));

        log.info("Account: " + String.valueOf(account));

        BigDecimal lastBalance = account.getCurrentBalance();

        log.info("Last Balance: " + String.valueOf(lastBalance));

        BigDecimal difference = calculateDifference(lastBalance, newAllocation.getBalance());

        log.info("Difference: " + String.valueOf(difference));

        validateAllocations(difference, newAllocation);

        Balance newBalance = balanceMapper.toBalanceFromInput(newAllocation);
        newBalance.setDifferenceFromLast(difference);
        newBalance.setReconcileDate(LocalDate.now());
        newBalance.setAccount(account);
        Balance savedBalance = balanceRepository.save(newBalance);

        account.setCurrentBalance(savedBalance.getBalance());
        accountRepository.save(account);

        log.info("Saved Balance: " + String.valueOf(savedBalance));

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
        log.info("Calculating Difference");
        return Optional.ofNullable(currentBalance)
                .orElse(BigDecimal.ZERO)
                .subtract(Optional.ofNullable(lastBalance).orElse(BigDecimal.ZERO));
    }

    private void validateAllocations(BigDecimal difference, BalanceAllocationDto allocation) {
        log.info("Validating Allocation");

        BigDecimal depositValue = Optional.ofNullable(allocation.getDepositValue()).orElse(BigDecimal.ZERO);
        log.info("Deposit Value: " + depositValue);
        BigDecimal withdrawalValue = Optional.ofNullable(allocation.getWithdrawalValue()).orElse(BigDecimal.ZERO);
        log.info("Withdrawal Value: " + withdrawalValue);
        BigDecimal interestValue = Optional.ofNullable(allocation.getInterestValue()).orElse(BigDecimal.ZERO);
        log.info("Interest Value: " + interestValue);
        BigDecimal feesValue = Optional.ofNullable(allocation.getFeesValue()).orElse(BigDecimal.ZERO);
        log.info("Fees Value: " + feesValue);
        BigDecimal bonusValue = Optional.ofNullable(allocation.getBonusValue()).orElse(BigDecimal.ZERO);
        log.info("Bonus Value: " + bonusValue);
        BigDecimal growthValue = Optional.ofNullable(allocation.getGrowthValue()).orElse(BigDecimal.ZERO);
        log.info("Growth Value: " + growthValue);

        BigDecimal totalAllocations = depositValue
                .add(interestValue)
                .add(bonusValue)
                .add(growthValue)
                .subtract(withdrawalValue)
                .subtract(feesValue);

        log.info("Allocation Value: " + totalAllocations);

        if (difference.compareTo(totalAllocations) != 0) {
            throw new InvalidAllocationException("Allocation amounts do not match expected total value");
        }
    }

    @Override
    public BalanceHistoryDto getBalanceHistory(String keycloakUserId, Long accountId) {

        Account account = accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found or does not belong to user"));

        log.info("Account: " + account);

        List<Balance> balanceHistory = balanceRepository.findByAccount(account);

        log.info("Balance History: " + balanceHistory);

        BalanceHistoryDto history = new BalanceHistoryDto();

        if (balanceHistory.isEmpty()) {
            history.setBalances(null);
            return history;
        }

        balanceHistory.sort((b1, b2) -> b2.getReconcileDate().compareTo(b1.getReconcileDate()));

        history.setBalances(balanceHistory);
        return history;
    }
}


package com.drew.accountservice.service.impl;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.entity.Account;
import com.drew.accountservice.kafka.KafkaSenderService;
import com.drew.accountservice.mapper.AccountMapper;
import com.drew.accountservice.repository.AccountRepository;
import com.drew.accountservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    @Autowired
    private KafkaSenderService kafkaSenderService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountOutputDto createAccount(String keycloakUserId, AccountInputDto accountInputDto) {

        log.info("Keycloak User ID: " + keycloakUserId);

        Account newAccount = accountMapper.toEntity(accountInputDto);

        newAccount.setDateCreated(LocalDateTime.now());
        newAccount.setDateUpdated(LocalDateTime.now());
        newAccount.setKeycloakId(keycloakUserId);

        Account savedAccount = accountRepository.save(newAccount);

        if (savedAccount.getIsa()) {
            String message = buildIsaAccountMessage(savedAccount);
            kafkaSenderService.sendKafkaMessage("new-isa-account-topic", message);
        }

        return accountMapper.toOutputDto(savedAccount);
    }

    @Override
    public List<AccountOutputDto> getUserAccounts(String keycloakUserId) {
        List<Account> accounts = accountRepository.findAllByKeycloakId(keycloakUserId);
        return accounts.stream()
                .map(accountMapper::toOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountOutputDto> getAccountByIdAndKeycloakId(Long accountId, String keycloakUserId) {
        Optional<Account> account = accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId);
        return account.map(accountMapper::toOutputDto);
    }

    @Override
    public Optional<AccountOutputDto> updateAccountByIdAndKeycloakId(Long accountId, String keycloakUserId, AccountInputDto accountInputDto) {
        return accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId)
                .map(account -> {
                    accountMapper.updateAccountFromInput(accountInputDto, account);
                    account.setDateUpdated(LocalDateTime.now());
                    Account updatedAccount = accountRepository.save(account);

                    // Kafka to  ISA Service
                    String message = buildIsaAccountMessage(updatedAccount);
                    if (account.getIsa() & !updatedAccount.getIsa()) {
                        // If account ISA status was true and now false
                        kafkaSenderService.sendKafkaMessage("remove-isa-account-topic", message);
                    } else if (!account.getIsa() & updatedAccount.getIsa()) {
                        // If account ISA status was false and now true
                        kafkaSenderService.sendKafkaMessage("new-isa-account-topic", message);
                    }


                    return accountMapper.toOutputDto(updatedAccount);
                });
    }


    @Override
    public boolean softDeleteAccount(Long accountId, String keycloakUserId) {
        return accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId)
                .map(account -> {
                    account.setStatus(Account.AccountStatus.ARCHIVED);
                    account.setDateUpdated(LocalDateTime.now());
                    accountRepository.save(account);
                    return true;
                }).orElse(false);
    }

    private String buildIsaAccountMessage(Account account) {
        return "Type: " + account.getType() + ", Account Number: " + account.getAccountId();
    }

}

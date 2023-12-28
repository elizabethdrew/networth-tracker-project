package com.drew.accountservice.service.impl;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.dto.AccountUpdateDto;
import com.drew.accountservice.entity.Account;
import com.drew.accountservice.kafka.KafkaService;
import com.drew.accountservice.mapper.AccountMapper;
import com.drew.accountservice.repository.AccountRepository;
import com.drew.accountservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final KafkaService kafkaService;


    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, KafkaService kafkaService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.kafkaService = kafkaService;
    }

    @Override
    public AccountOutputDto createAccount(String keycloakUserId, AccountInputDto accountInputDto) {

        log.info("Keycloak User ID: " + keycloakUserId);

        Account newAccount = accountMapper.toEntity(keycloakUserId, accountInputDto);

        newAccount.setDateCreated(LocalDateTime.now());
        newAccount.setDateUpdated(LocalDateTime.now());

        Account savedAccount = accountRepository.save(newAccount);

        // If ISA account, tell ISA Service
        if (savedAccount.getType().toString().contains("ISA")) {
            log.info("New account is an ISA");
            kafkaService.newAccountKafka("sendNewIsaAccount-out-0", savedAccount);
        } else {
            log.info("New account is not an ISA");
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
    public Optional<AccountOutputDto> updateAccountByIdAndKeycloakId(Long accountId, String keycloakUserId, AccountUpdateDto accountUpdateDto) {
        return accountRepository.findByAccountIdAndKeycloakId(accountId, keycloakUserId)
                .map(account -> {
                    accountMapper.updateAccount(accountUpdateDto, account);
                    account.setDateUpdated(LocalDateTime.now());
                    Account updatedAccount = accountRepository.save(account);

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

}

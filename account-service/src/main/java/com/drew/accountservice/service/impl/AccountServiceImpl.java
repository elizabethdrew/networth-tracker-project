package com.drew.accountservice.service.impl;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.dto.AccountsMsgDto;
import com.drew.accountservice.entity.Account;
import com.drew.accountservice.mapper.AccountMapper;
import com.drew.accountservice.repository.AccountRepository;
import com.drew.accountservice.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
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
    private final ObjectMapper objectMapper;
    private final StreamBridge streamBridge;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, ObjectMapper objectMapper, StreamBridge streamBridge) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.objectMapper = objectMapper;
        this.streamBridge = streamBridge;
    }

    @Override
    public AccountOutputDto createAccount(String keycloakUserId, AccountInputDto accountInputDto) {

        log.info("Keycloak User ID: " + keycloakUserId);

        Account newAccount = accountMapper.toEntity(accountInputDto);

        newAccount.setDateCreated(LocalDateTime.now());
        newAccount.setDateUpdated(LocalDateTime.now());
        newAccount.setKeycloakId(keycloakUserId);

        Account savedAccount = accountRepository.save(newAccount);

        // Send fake message to message service
        sendCommunication(savedAccount);

        return accountMapper.toOutputDto(savedAccount);
    }

    private void sendCommunication(Account account) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountId(), account.getAccountNickname(), account.getKeycloakId());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
        streamBridge.send("sendCommunication-out-0", accountsMsgDto);
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
                    if (account.getIsa() & !updatedAccount.getIsa()) {
                        // If account ISA status was true and now false

                    } else if (!account.getIsa() & updatedAccount.getIsa()) {
                        // If account ISA status was false and now true

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

}

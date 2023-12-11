package com.drew.accountservice.service;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.entity.Account;
import com.drew.accountservice.mapper.AccountMapper;
import com.drew.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountOutputDto createAccount(String keycloakUserId, AccountInputDto accountInputDto) {
        Account newAccount = accountMapper.toEntity(accountInputDto);

        newAccount.setDateCreated(LocalDateTime.now());
        newAccount.setDateUpdated(LocalDateTime.now());
        newAccount.setUserId(keycloakUserId);

        Account savedAccount = accountRepository.save(newAccount);

        if (newAccount.getIsa()) {
            // Tell ISA Service If New ISA Account
        }

        return accountMapper.toOutputDto(savedAccount);
    }
}

package com.drew.accountservice.service.impl;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.dto.AccountUpdateDto;
import com.drew.accountservice.entity.Account;
import com.drew.accountservice.mapper.AccountMapper;
import com.drew.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.drew.commonlibrary.types.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;
    private AccountInputDto inputDto;
    private Account accountEntity;
    private AccountOutputDto outputDto;

    private AccountUpdateDto updateDto;
    private String keycloakUserId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Common setup for all tests
        accountEntity = new Account();
        inputDto = new AccountInputDto();
        outputDto = new AccountOutputDto();
        updateDto = new AccountUpdateDto();

        keycloakUserId = "user123";
        String accountNickname = "Big Savings";
        String currency = "GBP";
        BigDecimal creditLimit = BigDecimal.valueOf(5000);
        Boolean isa = true;
        String notes = "Here are some notes";
        AccountType type = AccountType.SAVINGS_ACCOUNT;
        Account.AccountStatus status = Account.AccountStatus.IN_USE;
        LocalDateTime dateOpened = LocalDateTime.parse("2023-06-06T12:00:00");
        Boolean fixedTerm = true;
        LocalDateTime fixedTermEnd = LocalDateTime.parse("2023-06-06T12:00:00");
        Long percentageOwnership = 100L;
        LocalDateTime dateCreated = LocalDateTime.parse("2023-06-06T12:00:00");
        LocalDateTime dateUpdated = LocalDateTime.parse("2023-06-06T12:00:00");

        inputDto.setAccountNickname(accountNickname);
        inputDto.setCurrency(currency);
        inputDto.setCreditLimit(creditLimit);
        inputDto.setNotes(notes);
        inputDto.setType(type);
        inputDto.setStatus(status);
        inputDto.setDateOpened(dateOpened);
        inputDto.setFixedTerm(fixedTerm);
        inputDto.setFixedTermEndDate(fixedTermEnd);
        inputDto.setPercentageOwnership(percentageOwnership);

        updateDto.setAccountNickname(accountNickname);
        updateDto.setCreditLimit(creditLimit);
        updateDto.setNotes(notes);
        updateDto.setStatus(status);
        updateDto.setDateOpened(dateOpened);

        outputDto = new AccountOutputDto();
        outputDto.setAccountId(1L);
        outputDto.setAccountNickname(accountNickname);
        outputDto.setCurrency(currency);
        outputDto.setCreditLimit(creditLimit);
        outputDto.setNotes(notes);
        outputDto.setType(type);
        outputDto.setStatus(status);
        outputDto.setDateOpened(dateOpened);
        outputDto.setFixedTerm(fixedTerm);
        outputDto.setFixedTermEndDate(fixedTermEnd);
        outputDto.setPercentageOwnership(percentageOwnership);
        outputDto.setDateCreated(dateCreated);
        outputDto.setDateUpdated(dateUpdated);

        // Common mock behavior
        when(accountMapper.toEntity(any(AccountInputDto.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);
        when(accountMapper.toOutputDto(any(Account.class))).thenReturn(outputDto);
    }

    @Test
    void createAccountTest() {
        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void createAccountTestWithDifferentAccountTypes(AccountType accountType) {
        inputDto.setType(accountType);
        outputDto.setType(accountType);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(accountType, resultDto.getType(), "Account type in output should match input");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Some important note", ""})
    void createAccountTestWithOptionalFields(String notes) {
        inputDto.setNotes(notes);
        outputDto.setNotes(notes);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(notes, resultDto.getNotes(), "Notes in output should match input");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 50, 100})
    void createAccountTestWithPercentageOwnership(long percentageOwnership) {
        inputDto.setPercentageOwnership(percentageOwnership);
        outputDto.setPercentageOwnership(percentageOwnership);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(percentageOwnership, resultDto.getPercentageOwnership(), "Percentage Ownership in output should match input");
    }

    @ParameterizedTest
    @EnumSource(Account.AccountStatus.class)
    void createAccountTestWithDifferentStatuses(Account.AccountStatus status) {
        inputDto.setStatus(status);
        outputDto.setStatus(status);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(status, resultDto.getStatus(), "Status in output should match input");

    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void createAccountTestWithOptionalFixedTerm(Boolean fixedTerm) {
        inputDto.setFixedTerm(fixedTerm);
        outputDto.setFixedTerm(fixedTerm);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(fixedTerm, resultDto.getFixedTerm(), "Fixed Term in output should match input");
    }



}

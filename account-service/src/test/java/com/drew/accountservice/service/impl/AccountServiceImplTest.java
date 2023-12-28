package com.drew.accountservice.service.impl;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.dto.AccountUpdateDto;
import com.drew.accountservice.entity.Account;
import com.drew.accountservice.kafka.KafkaService;
import com.drew.accountservice.mapper.AccountMapper;
import com.drew.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private KafkaService kafkaService;
    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountInputDto inputDto;
    private Account accountEntity;
    private AccountOutputDto outputDto;
    private AccountUpdateDto updateDto;

    // Starting Values
    String keycloakUserId = "user123";
    String accountNickname = "Big Savings";
    String currency = "GBP";
    BigDecimal creditLimit = BigDecimal.valueOf(5000);
    BigDecimal currentBalance = BigDecimal.ZERO;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Common setup for all tests
        accountEntity = new Account();
        accountEntity.setAccountId(1L);
        accountEntity.setKeycloakId(keycloakUserId);
        accountEntity.setAccountNickname(accountNickname);
        accountEntity.setType(type);
        accountEntity.setCurrency(currency);
        accountEntity.setCreditLimit(creditLimit);
        accountEntity.setPercentageOwnership(percentageOwnership);
        accountEntity.setFixedTerm(fixedTerm);
        accountEntity.setFixedTermEndDate(fixedTermEnd);
        accountEntity.setDateOpened(dateOpened);
        accountEntity.setDateCreated(dateCreated);
        accountEntity.setDateUpdated(dateUpdated);
        accountEntity.setStatus(status);
        accountEntity.setNotes(notes);

    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void createAccountTestWithDifferentAccountTypes(AccountType type) {

        accountEntity.setType(type);

        inputDto = new AccountInputDto(
                accountNickname,
                type,
                currency,
                creditLimit,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                status,
                notes
        );

        outputDto = new AccountOutputDto(
                1L,
                accountNickname,
                type,
                currency,
                creditLimit,
                currentBalance,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                dateCreated,
                dateUpdated,
                status,
                notes
        );

        when(accountMapper.toEntity(eq(keycloakUserId), any(AccountInputDto.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);
        when(accountMapper.toOutputDto(any(Account.class))).thenReturn(outputDto);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        assertNotNull(resultDto, "Result DTO should not be null");
        assertEquals(type, resultDto.type(), "Account type in output should match input");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Some important note", ""})
    void createAccountTestWithOptionalFields(String notes) {

        accountEntity.setNotes(notes);

        inputDto = new AccountInputDto(
                accountNickname,
                type,
                currency,
                creditLimit,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                status,
                notes
        );
        outputDto = new AccountOutputDto(
                1L,
                accountNickname,
                type,
                currency,
                creditLimit,
                currentBalance,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                dateCreated,
                dateUpdated,
                status,
                notes
        );

        when(accountMapper.toEntity(eq(keycloakUserId), any(AccountInputDto.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);
        when(accountMapper.toOutputDto(any(Account.class))).thenReturn(outputDto);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(notes, resultDto.notes(), "Notes in output should match input");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 50, 100})
    void createAccountTestWithPercentageOwnership(long percentageOwnership) {

        accountEntity.setPercentageOwnership(percentageOwnership);

        inputDto = new AccountInputDto(
                accountNickname,
                type,
                currency,
                creditLimit,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                status,
                notes
        );
        outputDto = new AccountOutputDto(
                1L,
                accountNickname,
                type,
                currency,
                creditLimit,
                currentBalance,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                dateCreated,
                dateUpdated,
                status,
                notes
        );

        when(accountMapper.toEntity(eq(keycloakUserId), any(AccountInputDto.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);
        when(accountMapper.toOutputDto(any(Account.class))).thenReturn(outputDto);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(percentageOwnership, resultDto.percentageOwnership(), "Percentage Ownership in output should match input");
    }

    @ParameterizedTest
    @EnumSource(Account.AccountStatus.class)
    void createAccountTestWithDifferentStatuses(Account.AccountStatus status) {

        accountEntity.setStatus(status);

        inputDto = new AccountInputDto(
                accountNickname,
                type,
                currency,
                creditLimit,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                status,
                notes
        );
        outputDto = new AccountOutputDto(
                1L,
                accountNickname,
                type,
                currency,
                creditLimit,
                currentBalance,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                dateCreated,
                dateUpdated,
                status,
                notes
        );

        when(accountMapper.toEntity(eq(keycloakUserId), any(AccountInputDto.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);
        when(accountMapper.toOutputDto(any(Account.class))).thenReturn(outputDto);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(status, resultDto.status(), "Status in output should match input");

    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void createAccountTestWithOptionalFixedTerm(Boolean fixedTerm) {

        accountEntity.setFixedTerm(fixedTerm);

        inputDto = new AccountInputDto(
                accountNickname,
                type,
                currency,
                creditLimit,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                status,
                notes
        );
        outputDto = new AccountOutputDto(
                1L,
                accountNickname,
                type,
                currency,
                creditLimit,
                currentBalance,
                percentageOwnership,
                fixedTerm,
                fixedTermEnd,
                dateOpened,
                dateCreated,
                dateUpdated,
                status,
                notes
        );

        when(accountMapper.toEntity(eq(keycloakUserId), any(AccountInputDto.class))).thenReturn(accountEntity);
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);
        when(accountMapper.toOutputDto(any(Account.class))).thenReturn(outputDto);

        AccountOutputDto resultDto = accountService.createAccount(keycloakUserId, inputDto);

        verify(accountRepository).save(accountEntity);
        verify(accountMapper).toOutputDto(accountEntity);
        assertEquals(fixedTerm, resultDto.fixedTerm(), "Fixed Term in output should match input");
    }

}

package com.drew.accountservice.service;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountOutputDto createAccount(String keycloakUserId, AccountInputDto accountInputDto);

    List<AccountOutputDto> getUserAccounts(String keycloakUserId);

    Optional<AccountOutputDto> getAccountByIdAndKeycloakId(Long accountId, String keycloakUserId);

    Optional<AccountOutputDto> updateAccountByIdAndKeycloakId(Long accountId, String keycloakUserId, AccountInputDto accountInputDto);

    boolean softDeleteAccount(Long accountId, String keycloakUserId);
}

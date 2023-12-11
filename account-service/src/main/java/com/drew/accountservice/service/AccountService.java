package com.drew.accountservice.service;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AccountService {
    AccountOutputDto createAccount(String keycloakUserId, AccountInputDto accountInputDto);
}

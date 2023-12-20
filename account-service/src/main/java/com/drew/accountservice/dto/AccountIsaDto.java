package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Account;

public record AccountIsaDto(Long accountId, Account.AccountType type, String keycloakId ) {
}
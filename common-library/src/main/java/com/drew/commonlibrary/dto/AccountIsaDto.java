package com.drew.commonlibrary.dto;

import com.drew.commonlibrary.types.AccountType;

public record AccountIsaDto(
        Long accountId,
        AccountType accountType,
        String keycloakId ) {

}


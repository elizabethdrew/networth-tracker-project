package com.drew.accountservice.dto;

/**
 * @param accountId
 * @param accountNickname
 * @param keycloakId
 */
public record AccountMsgDto(Long accountId, String accountNickname, String keycloakId ) {
}
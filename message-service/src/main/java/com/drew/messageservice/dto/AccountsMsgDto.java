package com.drew.messageservice.dto;

/**
 * @param accountId
 * @param accountNickname
 * @param keycloakId
 */
public record AccountsMsgDto(Long accountId, String accountNickname, String keycloakId ) {
}
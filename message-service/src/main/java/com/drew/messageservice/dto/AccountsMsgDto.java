package com.drew.messageservice.dto;

/**
 * @param accountNumber
 * @param accountNickname
 * @param keycloakId
 */
public record AccountsMsgDto(Long accountNumber, String accountNickname, String keycloakId ) {
}
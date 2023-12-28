package com.drew.accountservice.mapper;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.dto.AccountUpdateDto;
import com.drew.accountservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "keycloakId", source = "keycloakUserId")
    Account toEntity(String keycloakUserId, AccountInputDto dto);

    AccountOutputDto toOutputDto(Account account);

    void updateAccount(AccountUpdateDto input, @MappingTarget Account account);
}

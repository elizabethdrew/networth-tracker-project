package com.drew.accountservice.mapper;

import com.drew.accountservice.dto.AccountInputDto;
import com.drew.accountservice.dto.AccountOutputDto;
import com.drew.accountservice.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    Account toEntity(AccountInputDto dto);

    AccountOutputDto toOutputDto(Account account);
}

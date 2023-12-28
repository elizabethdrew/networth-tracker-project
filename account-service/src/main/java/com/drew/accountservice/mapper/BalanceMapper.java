package com.drew.accountservice.mapper;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.dto.BalanceDto;
import com.drew.accountservice.entity.Balance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    Balance toBalanceFromInput(BalanceAllocationDto dto);

    BalanceDto toBalanceDto(Balance balance);
}

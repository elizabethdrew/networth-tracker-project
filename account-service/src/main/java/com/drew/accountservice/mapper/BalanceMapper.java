package com.drew.accountservice.mapper;

import com.drew.accountservice.dto.BalanceAllocationDto;
import com.drew.accountservice.entity.Balance;
import com.drew.commonlibrary.dto.KafkaBalanceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    Balance toBalanceFromInput(BalanceAllocationDto dto);

    KafkaBalanceDto toKafkaBalanceDto(Balance balance);
}

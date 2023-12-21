package com.drew.isaservice.service;

import com.drew.commonlibrary.dto.AccountIsaDto;
import com.drew.commonlibrary.dto.KafkaBalanceDto;

public interface IsaService {

    void addIsaAccountToIsaService(AccountIsaDto accountIsaDto);

    void addIsaBalance(KafkaBalanceDto balance);
}

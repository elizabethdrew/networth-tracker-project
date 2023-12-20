package com.drew.isaservice.service.impl;

import com.drew.isaservice.dto.AccountIsaDto;
import com.drew.isaservice.service.IsaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IsaServiceImpl implements IsaService {
    @Override
    public void addIsaAccountToIsaService(AccountIsaDto accountIsaDto) {
        log.info("Adding new ISA account: " + accountIsaDto.toString());
    }
}

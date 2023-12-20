package com.drew.isaservice.functions;

import com.drew.isaservice.dto.AccountIsaDto;
import com.drew.isaservice.service.IsaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class IsaFunctions {
    @Bean
    public Consumer<AccountIsaDto> addIsaAccount(IsaService isaService) {
        return account -> {
            isaService.addIsaAccountToIsaService(account);
        };
    }
}

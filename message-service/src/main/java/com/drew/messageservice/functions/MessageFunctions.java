package com.drew.messageservice.functions;

import com.drew.messageservice.dto.AccountsMsgDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Slf4j
@Configuration
public class MessageFunctions {

    @Bean
    public Function<AccountsMsgDto,AccountsMsgDto> email() {
        return accountsMsgDto -> {
            log.info("Sending email with the details : " + accountsMsgDto.toString());
            return accountsMsgDto;
        };
    }

    @Bean
    public Function<AccountsMsgDto,Long> sms() {
        return accountsMsgDto -> {
            log.info("Sending sms with the details : " +  accountsMsgDto.toString());
            return accountsMsgDto.accountNumber();
        };
    }

}

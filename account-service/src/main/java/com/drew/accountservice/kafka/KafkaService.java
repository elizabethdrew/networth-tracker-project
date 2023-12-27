package com.drew.accountservice.kafka;

import com.drew.accountservice.entity.Account;
import com.drew.accountservice.entity.Balance;
import com.drew.commonlibrary.dto.AccountIsaDto;
import com.drew.commonlibrary.dto.KafkaBalanceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class KafkaService {
    private final StreamBridge streamBridge;

    public KafkaService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void newAccountKafka(String topic, Account account) {
        var accountIsaDto = new AccountIsaDto(account.getAccountId(), account.getType(), account.getKeycloakId());
        log.info("Sending to Isa Service - New Isa Account: {}", accountIsaDto);
        streamBridge.send(topic, accountIsaDto);
    }

    public void newBalanceKafka(String topic, Balance balance, String keycloakId) {
        var kafkaBalanceDto = new KafkaBalanceDto(
                balance.getAccount().getAccountId(),
                keycloakId,
                balance.getAccount().getType(),
                balance.getBalance(),
                balance.getDepositValue(),
                balance.getWithdrawalValue()
        );
        log.info("Alerting isa service about balance update: " + kafkaBalanceDto);
        streamBridge.send(topic, kafkaBalanceDto);

    }
}

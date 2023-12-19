package com.drew.isaservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "new-isa-account-topic", groupId = "isa-group")
    public void newIsaAccount(String message) {
        log.info("Received message in ISA service - Add New ISA: " + message);

        // Implement the logic to handle the new ISA account creation
    }

    @KafkaListener(topics = "remove-isa-account-topic", groupId = "isa-group")
    public void removeIsaAccount(String message) {
        log.info("Received message in ISA service - Remove ISA: " + message);

        // Implement the logic to handle the new ISA account creation
    }
}


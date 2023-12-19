package com.drew.accountservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSenderService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}

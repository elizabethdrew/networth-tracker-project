package com.drew.isaservice.exception;

import org.apache.kafka.common.errors.ResourceNotFoundException;

public class LisaDepositException extends ResourceNotFoundException {
    public LisaDepositException(String message) {
        super(message);
    }
}
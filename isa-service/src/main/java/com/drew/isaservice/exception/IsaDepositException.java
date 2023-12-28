package com.drew.isaservice.exception;

import org.apache.kafka.common.errors.ResourceNotFoundException;

public class IsaDepositException extends ResourceNotFoundException {
    public IsaDepositException(String message) {
        super(message);
    }
}
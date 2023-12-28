package com.drew.accountservice.exception;

public class BalanceNotFoundException extends ResourceNotFoundException {
    public BalanceNotFoundException(String message) {
        super(message);
    }
}
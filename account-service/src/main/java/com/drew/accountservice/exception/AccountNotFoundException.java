package com.drew.accountservice.exception;

public class AccountNotFoundException extends ResourceNotFoundException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
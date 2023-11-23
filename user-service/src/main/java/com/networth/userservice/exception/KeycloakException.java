package com.networth.userservice.exception;

import feign.FeignException;

public class KeycloakException extends RuntimeException {
    public KeycloakException(String message) {
        super(message);
    }
}

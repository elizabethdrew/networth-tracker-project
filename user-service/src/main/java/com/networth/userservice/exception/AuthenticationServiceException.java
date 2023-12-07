package com.networth.userservice.exception;

import feign.FeignException;
import org.springframework.dao.DataAccessException;

public class AuthenticationServiceException extends RuntimeException {
    public AuthenticationServiceException(String message, Exception e) {
        super(message, e);
    }
}

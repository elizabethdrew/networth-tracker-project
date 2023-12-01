package com.networth.userservice.exception;

import feign.FeignException;
import org.springframework.dao.DataAccessException;

public class AuthenticationServiceException extends DataAccessException {
    public AuthenticationServiceException(String message, FeignException e) {
        super(message, e);
    }
}

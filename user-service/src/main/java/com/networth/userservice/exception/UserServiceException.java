package com.networth.userservice.exception;

import org.springframework.dao.DataAccessException;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message, DataAccessException e) {
        super(message);
    }
}

package com.networth.userservice.exception;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message, Exception e) {
        super(message);
    }
}

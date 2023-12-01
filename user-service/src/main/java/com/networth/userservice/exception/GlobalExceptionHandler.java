package com.networth.userservice.exception;

import com.networth.userservice.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> generateErrorResponse(HttpStatus status, Exception e) {
        log.warn(String.valueOf(e));
        ErrorResponse error = new ErrorResponse();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return new ResponseEntity<>(error, status);
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(HttpStatus status, String message, Exception e) {
        log.warn(message, e);
        ErrorResponse error = new ErrorResponse();
        error.setCode(status.value());
        error.setMessage(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return generateErrorResponse(HttpStatus.NOT_FOUND, "Not Found", e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return generateErrorResponse(HttpStatus.NOT_FOUND, "User not found", e);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException e) {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Input", e);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Credentials", e);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException e) {
        return generateErrorResponse(HttpStatus.CONFLICT, "Duplicate Input", e);
    }

    @ExceptionHandler(InsufficientPermissionException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPermissionException(InsufficientPermissionException e) {
        return generateErrorResponse(HttpStatus.FORBIDDEN, "Insufficient Permissions", e);
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakException(KeycloakException e) {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Keycloak Error", e);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorResponse> handleUserServiceException(UserServiceException e) {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "User Service Error", e);
    }

}

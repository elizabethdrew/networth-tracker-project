package com.networth.userservice.util;

import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HelperUtilsTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HelperUtils helperUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidatePasswordSuccess() {
        String validPassword = "Valid123!";

        assertDoesNotThrow(() -> helperUtils.validatePassword(validPassword));
    }

    @Test
    public void testValidatePasswordFailure() {
        String invalidPassword = "short";

        Exception exception = assertThrows(InvalidInputException.class, () -> helperUtils.validatePassword(invalidPassword));
        assertTrue(exception.getMessage().contains("Invalid password"));
    }

    @Test
    public void testValidateUsernameUniqueSuccess() {
        String uniqueUsername = "uniqueUsername";
        when(userRepository.existsByUsername(uniqueUsername)).thenReturn(false);

        assertDoesNotThrow(() -> helperUtils.validateUsernameUnique(uniqueUsername));
    }

    @Test
    public void testValidateUsernameUniqueFailure() {
        String existingUsername = "existingUsername";
        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

        Exception exception = assertThrows(InvalidInputException.class, () -> helperUtils.validateUsernameUnique(existingUsername));
        assertEquals("Username already in use: " + existingUsername, exception.getMessage());
    }

    @Test
    public void testValidateEmailUniqueSuccess() {
        String uniqueEmail = "unique@example.com";
        when(userRepository.existsByEmail(uniqueEmail)).thenReturn(false);

        assertDoesNotThrow(() -> helperUtils.validateEmailUnique(uniqueEmail));
    }

    @Test
    public void testValidateEmailUniqueFailure() {
        String existingEmail = "existing@example.com";
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        Exception exception = assertThrows(InvalidInputException.class, () -> helperUtils.validateEmailUnique(existingEmail));
        assertEquals("Email already in use: " + existingEmail, exception.getMessage());
    }
}

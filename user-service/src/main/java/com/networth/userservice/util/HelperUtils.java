package com.networth.userservice.util;

import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HelperUtils {

    private final UserRepository userRepository;

    public HelperUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Validate Password Against Rules
    public void validatePassword(String password) {

        // Define rules
        List<Rule> rules = new ArrayList<>();
        rules.add(new LengthRule(8, 100)); // password length between 8 and 100
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1)); // at least 1 uppercase character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1)); // at least 1 lowercase character
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1)); // at least 1 digit
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1)); // at least 1 special character

        PasswordValidator validator = new PasswordValidator(rules);
        RuleResult result = validator.validate(new PasswordData(password));

        if (!result.isValid()) {
            String message = String.join(", ", validator.getMessages(result));
            throw new InvalidInputException("Invalid password: " + message);
        }

        log.debug("Password validation passed");
    }

    public void validateUsernameUnique(String username) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            log.warn("Registration failed: Username '{}' is already in use.", username);
            throw new InvalidInputException("Username already in use: " + username);
        }
    }

    public void validateEmailUnique(String email) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            log.warn("Registration failed: Email '{}' is already in use.", email);
            throw new InvalidInputException("Email already in use: " + email);
        }
    }
}

package com.networth.userservice.service.impl;

import com.networth.userservice.dto.UserInput;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.UserService;
import com.networth.userservice.util.HelperUtils;
import jakarta.transaction.Transactional;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final HelperUtils helperUtils;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, HelperUtils helperUtils, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.helperUtils = helperUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public UserOutput getUser(Long userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id not found: " + userId));

        // Add security to check user is only viewing own profile

        // If deleted send different output?

        return userMapper.toUserOutput(user);
    }

    @Transactional
    public UserOutput createUser(UserInput userInput) {

        // Check if username already exists
        if(userRepository.existsByUsername(userInput.getUsername())) {
            throw new InvalidInputException("Username already in use: " + userInput.getUsername());
        }

        // Check if email already exists
        if(userRepository.existsByEmail(userInput.getEmail())) {
            throw new InvalidInputException("Email already in use: " + userInput.getEmail());
        }

        // Validate Password
        helperUtils.validatePassword(userInput.getPassword());

        // Create New User
        User user = userMapper.toUser(userInput);
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        user.setDateOpened(LocalDateTime.now());
        user.setActiveUser(true);
        User savedUser = userRepository.save(user);

        return userMapper.toUserOutput(savedUser);
    }


    @Transactional
    public UserOutput updateUser(Long userId, UserInput userInput)
            throws UserNotFoundException {

        // Retrieve the user with the specified ID from the repository
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id not found: " + userId));

        // Add security to check user is only editing own profile

        // Create updated user
        User updatedUser = userMapper.toUser(userInput);
        updatedUser.setUserId(user.getUserId());
        updatedUser.setDateOpened(user.getDateOpened());
        updatedUser.setDateOpened(user.getDateOpened());

        // Check if new password is provided
        if(userInput.getPassword() != null) {
            // Encrypt new password
            String encryptedPassword = passwordEncoder.encode(userInput.getPassword());
            updatedUser.setPassword(encryptedPassword);
        } else {
            updatedUser.setPassword(user.getPassword());
        }

        User savedUser = userRepository.save(updatedUser);
        return userMapper.toUserOutput(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {

        // Retrieve the user with the specified ID from the repository
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id not found: " + userId));

        // Add security to check user is only editing own profile

        // Mark the user as deleted
        user.setActiveUser(false);

        // Save the updated user entity
        userRepository.save(user);
    }


}

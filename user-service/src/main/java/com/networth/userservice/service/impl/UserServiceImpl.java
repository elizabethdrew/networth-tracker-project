package com.networth.userservice.service.impl;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.UserService;
import com.networth.userservice.util.HelperUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final HelperUtils helperUtils;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, HelperUtils helperUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.helperUtils = helperUtils;
    }

    public UserOutput getUser(Long userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id not found: " + userId));

        // Add security to check user is only viewing own profile

        // If deleted send different output?

        return userMapper.toUserOutput(user);
    }

    @Transactional
    public UserOutput registerUser(RegisterDto registerDto) {

        // Check if username already exists
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            throw new InvalidInputException("Username already in use: " + registerDto.getUsername());
        }

        // Check if email already exists
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new InvalidInputException("Email already in use: " + registerDto.getEmail());
        }

        // Validate Password
        helperUtils.validatePassword(registerDto.getPassword());

        // Create New User
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setDateOpened(LocalDateTime.now());
        user.setActiveUser(true);
        user.setKeycloakId("keycloakid");
        User savedUser = userRepository.save(user);

        return userMapper.toUserOutput(savedUser);
    }


    @Transactional
    public UserOutput updateUser(Long userId, RegisterDto registerDto)
            throws UserNotFoundException {

        // Retrieve the user with the specified ID from the repository
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User Id not found: " + userId));

        // Add security to check user is only editing own profile

        // Create updated user
        User updatedUser = userMapper.toUser(registerDto);
        updatedUser.setUserId(user.getUserId());
        updatedUser.setDateOpened(user.getDateOpened());
        updatedUser.setDateOpened(user.getDateOpened());

//        // Check if new password is provided
//        if(registerDto.getPassword() != null) {
//            // Encrypt new password
//            String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
//            updatedUser.setPassword(encryptedPassword);
//        } else {
//            updatedUser.setPassword(user.getPassword());
//        }

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

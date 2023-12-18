package com.networth.userservice.service.impl;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UpdateUserDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.exception.UserServiceException;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.KeycloakService;
import com.networth.userservice.service.UserService;
import com.networth.userservice.util.HelperUtils;
import feign.Response;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HelperUtils helperUtils;
    private final KeycloakService keycloakService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, HelperUtils helperUtils, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.helperUtils = helperUtils;
        this.keycloakService = keycloakService;
    }

    public UserOutput getUser(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Keycloak ID not found: " + keycloakId));
        return userMapper.toUserOutput(user);
    }

    @Transactional
    public UserOutput registerUser(RegisterDto registerDto) {
        try {
            log.info("Starting registration for username: {}", registerDto.getUsername());


            // Validate Username and Email Unique
            helperUtils.validateUsernameUnique(registerDto.getUsername());
            helperUtils.validateEmailUnique(registerDto.getEmail());

            // Validate Password
            helperUtils.validatePassword(registerDto.getPassword());

            // Create Keycloak User
            Response response = keycloakService.createUser(registerDto);
            String keycloakUserId = keycloakService.extractKeycloakUserId(response)
                    .orElseThrow(() -> new KeycloakException("Location header is missing in response from Keycloak"));

            // Create DB user
            User user = createUserEntity(registerDto, keycloakUserId);
            log.info("User '{}' successfully registered with Keycloak ID: {}", registerDto.getUsername(), keycloakUserId);
            return userMapper.toUserOutput(user);

        } catch (InvalidInputException | KeycloakException e) {
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred during user registration", e);
            throw new ServiceException("An unexpected error occurred during registration", e);
        }
    }

    @Transactional
    public UserOutput updateUser(String keycloakId, UpdateUserDto updateUserDto)
            throws UserNotFoundException {

        log.info("Starting update user");
        try {
            // Retrieve the user with the specified ID from the repository
            User user = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UserNotFoundException("Keycloak ID not found: " + keycloakId));

            // Map to updated user
            userMapper.updateUserFromDto(updateUserDto, user);
            user.setDateUpdated(LocalDateTime.now());


            // Update User in Keycloak if email has changed
            if (!user.getEmail().equals(updateUserDto.getEmail())) {
                // Check new email unique
                helperUtils.validateEmailUnique(updateUserDto.getEmail());
                // Update email in Keycloak
                keycloakService.updateEmailKeycloak(updateUserDto.getEmail(), keycloakId);
            }

            // Save updated user in the repository
            User savedUser = userRepository.save(user);
            return userMapper.toUserOutput(savedUser);

        } catch (UserNotFoundException e ){
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred during update user", e);
            throw new ServiceException("An unexpected error occurred during update user", e);
        }
    }

    @Transactional
    public void deleteUser(String keycloakId) {

        log.info("Starting soft delete user");

        // Retrieve the user with the specified ID from the repository
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("Keycloak ID not found: " + keycloakId));

        // Mark the user as deleted
        user.setActiveUser(false);

        // Save the updated user entity
        userRepository.save(user);
    }

    private User createUserEntity(RegisterDto registerDto, String keycloakUserId) {
        log.info("Creating user entity for username: {}", registerDto.getUsername());

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setDateOpened(LocalDateTime.now());
        user.setActiveUser(true);
        user.setKeycloakId(keycloakUserId);

        try {
            User savedUser = userRepository.save(user);
            log.info("User entity saved with ID: {}", savedUser.getUserId());
            return savedUser;
        } catch (DataAccessException e) {
            log.error("Error occurred while saving user entity for username: {}", registerDto.getUsername(), e);
            throw new UserServiceException("Failed to save user entity", e);
        }
    }


}

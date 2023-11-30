package com.networth.userservice.service.impl;

import com.networth.userservice.dto.PasswordCredentialDto;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UpdateUserDto;
import com.networth.userservice.dto.UpdateKeycloakDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.dto.UserRepresentationDto;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.AuthenticationServiceException;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.exception.UserServiceException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.UserService;
import com.networth.userservice.util.HelperUtils;
import feign.FeignException;
import feign.Response;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HelperUtils helperUtils;

    private final KeycloakClient keycloakClient;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, HelperUtils helperUtils, KeycloakClient keycloakClient) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.helperUtils = helperUtils;
        this.keycloakClient = keycloakClient;
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

            // Check if username already exists
            if (userRepository.existsByUsername(registerDto.getUsername())) {
                log.warn("Registration failed: Username '{}' is already in use.", registerDto.getUsername());
                throw new InvalidInputException("Username already in use: " + registerDto.getUsername());
            }

            // Check if email already exists
            if (userRepository.existsByEmail(registerDto.getEmail())) {
                log.warn("Registration failed: Email '{}' is already in use.", registerDto.getEmail());
                throw new InvalidInputException("Email already in use: " + registerDto.getEmail());
            }

            // Validate Password
            helperUtils.validatePassword(registerDto.getPassword());

            // Get Admin Access Token
            String accessToken = helperUtils.getAdminAccessToken();
            Map<String, Object> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);

            log.info("Registering user '{}' with Keycloak.", registerDto.getUsername());

            UserRepresentationDto formData = createUserRepresentation(registerDto);

            log.info("Adding User To Keycloak");
            Response response = keycloakClient.createKeycloakUser(headers, formData);

            if (response.status() == HttpStatus.CREATED.value()) {
                String keycloakUserId = extractKeycloakUserId(response);
                User user = createUserEntity(registerDto, keycloakUserId);
                user = userRepository.save(user);
                log.info("User '{}' successfully registered with Keycloak ID: {}", registerDto.getUsername(), keycloakUserId);
                return userMapper.toUserOutput(user);
            } else {
                log.error("Failed to create user in Keycloak. Status: {}, Body: {}", response.status(), response.body());
                throw new KeycloakException("Failed to create user in Keycloak. Status: " + response.status());
            }
        } catch (InvalidInputException e) {
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred during user registration", e);
            throw new ServiceException("An unexpected error occurred during registration", e);
        }
    }


    private UserRepresentationDto createUserRepresentation(RegisterDto registerDto) {
        log.debug("Constructing UserRepresentationDto for username: {}", registerDto.getUsername());

        UserRepresentationDto formData = new UserRepresentationDto();
        formData.setUsername(registerDto.getUsername());
        formData.setEmail(registerDto.getEmail());
        formData.setEnabled(true);

        PasswordCredentialDto password = new PasswordCredentialDto();
        password.setType("PASSWORD");
        password.setValue(registerDto.getPassword());
        password.setTemporary(false);

        formData.setCredentials(Collections.singletonList(password));

        log.debug("UserRepresentationDto constructed for username: {}", registerDto.getUsername());
        return formData;
    }

    private String extractKeycloakUserId(Response response) {
        log.debug("Extracting Keycloak User Id from response");

        if (response.status() == HttpStatus.CREATED.value()) {
            String locationHeader = response.headers().get("Location").stream().findFirst()
                    .orElseThrow(() -> new KeycloakException("Location header is missing in the response from Keycloak."));

            URI locationURI = URI.create(locationHeader);
            String path = locationURI.getPath();
            String keycloakUserId = path.substring(path.lastIndexOf('/') + 1);

            if (keycloakUserId.isEmpty()) {
                throw new KeycloakException("Extracted Keycloak User Id is null or empty.");
            }

            log.info("Extracted Keycloak User Id: {}", keycloakUserId);
            return keycloakUserId;
        } else {
            throw new KeycloakException("Expected status 201 but received " + response.status());
        }
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




    @Transactional
    public UserOutput updateUser(String keycloakId, UpdateUserDto updateUserDto)
            throws UserNotFoundException {

        log.info("Starting update user");
        try {
            // Retrieve the user with the specified ID from the repository
            User user = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UserNotFoundException("Keycloak ID not found: " + keycloakId));

            boolean emailChanged = !user.getEmail().equals(updateUserDto.getEmail());

            // Map to updated user
            User updatedUser = userMapper.toUpdateUser(updateUserDto);
            updatedUser.setUserId(user.getUserId());
            updatedUser.setUsername(user.getUsername());
            updatedUser.setKeycloakId(user.getKeycloakId());
            updatedUser.setActiveUser(user.getActiveUser());
            updatedUser.setDateOpened(user.getDateOpened());
            updatedUser.setDateUpdated(LocalDateTime.now());

            // Update User in Keycloak if email has changed
            if (emailChanged) {
                updateEmailKeycloak(updateUserDto.getEmail(), keycloakId);
            }

            // Save updated user in the repository
            User savedUser = userRepository.save(updatedUser);
            return userMapper.toUserOutput(savedUser);

        } catch (Exception e) {
            log.error("An unexpected error occurred during update user", e);
            throw new ServiceException("An unexpected error occurred during update user", e);
        }
    }

    private void updateEmailKeycloak(String email, String keycloakId) {
        log.info("Starting Update User in Keycloak");

        try {
            String accessToken = helperUtils.getAdminAccessToken();
            Map<String, Object> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);

            UpdateKeycloakDto formData = new UpdateKeycloakDto();
            formData.setEmail(email);
            Response response = keycloakClient.updateKeycloakUser(headers, keycloakId, formData);

            if (response.status() != HttpStatus.NO_CONTENT.value()) {
                log.error("Failed to update user in Keycloak. Status: {}, Body: {}", response.status(), response.body());
                throw new KeycloakException("Failed to update user in Keycloak. Status: " + response.status());
            }
            log.info("Email updated in Keycloak successfully");
        } catch (FeignException e) {
            log.error("Error communicating with Keycloak: ", e);
            throw new AuthenticationServiceException("Error communicating with Keycloak", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during email update in Keycloak", e);
            throw new UserServiceException("Unexpected error during email update in Keycloak", e);
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


}

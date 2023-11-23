package com.networth.userservice.service.impl;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.PasswordCredentialDto;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.dto.UserRepresentationDto;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.exception.UserServiceException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.UserService;
import com.networth.userservice.util.HelperUtils;
import feign.Feign;
import feign.Response;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
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
    private final KeycloakProperties keycloakProperties;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, HelperUtils helperUtils, KeycloakProperties keycloakProperties) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.helperUtils = helperUtils;
        this.keycloakProperties = keycloakProperties;
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

            KeycloakClient keycloakClient = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .target(KeycloakClient.class, keycloakProperties.getBaseUri());

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




//    @Transactional
//    public UserOutput updateUser(Long userId, RegisterDto registerDto)
//            throws UserNotFoundException {
//
//        // Retrieve the user with the specified ID from the repository
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new UserNotFoundException("User Id not found: " + userId));
//
//        // Add security to check user is only editing own profile
//
//        // Create updated user
//        User updatedUser = userMapper.toUser(registerDto);
//        updatedUser.setUserId(user.getUserId());
//        updatedUser.setDateOpened(user.getDateOpened());
//        updatedUser.setDateOpened(user.getDateOpened());
//
////        // Check if new password is provided
////        if(registerDto.getPassword() != null) {
////            // Encrypt new password
////            String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
////            updatedUser.setPassword(encryptedPassword);
////        } else {
////            updatedUser.setPassword(user.getPassword());
////        }
//
//        User savedUser = userRepository.save(updatedUser);
//        return userMapper.toUserOutput(savedUser);
//    }

//    @Transactional
//    public void deleteUser(Long userId) {
//
//        // Retrieve the user with the specified ID from the repository
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new UserNotFoundException("User Id not found: " + userId));
//
//        // Add security to check user is only editing own profile
//
//        // Mark the user as deleted
//        user.setActiveUser(false);
//
//        // Save the updated user entity
//        userRepository.save(user);
//    }


}

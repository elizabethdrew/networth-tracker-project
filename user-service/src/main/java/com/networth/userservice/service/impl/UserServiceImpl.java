package com.networth.userservice.service.impl;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.KeycloakAccessDto;
import com.networth.userservice.dto.PasswordCredentialDto;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.dto.UserRepresentationDto;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.feign.KeycloakClient;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.UserService;
import com.networth.userservice.util.HelperUtils;
import feign.Feign;
import feign.Response;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

        log.info("Register User Flow Started");

        // Check if username already exists
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new InvalidInputException("Username already in use: " + registerDto.getUsername());
        }

        log.info("Username Passed Checks");

        // Check if email already exists
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new InvalidInputException("Email already in use: " + registerDto.getEmail());
        }

        log.info("Email Passed Checks");

        // Validate Password
        helperUtils.validatePassword(registerDto.getPassword());

        log.info("Password Passed Checks");

        // Get Admin Access Token

        String accessToken = helperUtils.getAdminAccessToken();
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        log.info(headers.toString());

        log.info("Creating User Representation");

        KeycloakClient keycloakClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(KeycloakClient.class, keycloakProperties.getBaseUri());

        // Create User Representation
        UserRepresentationDto formData = new UserRepresentationDto();
        formData.setUsername(registerDto.getUsername());
        formData.setEmail(registerDto.getEmail());
        formData.setEnabled(true);

        log.info("Creating User Password Credential");

        PasswordCredentialDto password = new PasswordCredentialDto();
        password.setType("PASSWORD");
        password.setValue(registerDto.getPassword());
        password.setTemporary(false);

        formData.setCredentials(Collections.singletonList(password));

        log.info("Adding User To Keycloak");

        log.info(String.valueOf(formData));

        Response response = keycloakClient.createKeycloakUser(headers, formData);

        if (response.status() != 201) {
            log.error("Error response from Keycloak: Status Code: {}, Body: {}", response.status(), response);
            throw new KeycloakException("Unable to create user in Keycloak. Status: " + response.status());
        }

        log.info("Extracting Keycloak User Id");

        if(response.status() == 201) {
            String locationHeader = response.headers().get("Location").stream().findFirst().orElse(null);

            if(locationHeader == null) {
                throw new KeycloakException("Location header is missing in the response from Keycloak.");
            }

            URI locationURI = URI.create(locationHeader);
            String path = locationURI.getPath();
            String keycloakUserId = path.substring(path.lastIndexOf('/') + 1);

            if (keycloakUserId.isEmpty()) {
                log.error("Keycloak User Id is Null");
                throw new KeycloakException("Keycloak User Id is Null");
            }

            // Create New User
            log.info("Creating User");
            User user = new User();
            user.setUsername(registerDto.getUsername());
            user.setEmail(registerDto.getEmail());
            user.setDateOpened(LocalDateTime.now());
            user.setActiveUser(true);
            user.setKeycloakId(keycloakUserId);

            // Save user to the repository
            log.info("Saving User");
            user = userRepository.save(user);

            return userMapper.toUserOutput(user);

        } else {
            log.error("That didn't work. Sheet.");
            throw new KeycloakException("Bugger");
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

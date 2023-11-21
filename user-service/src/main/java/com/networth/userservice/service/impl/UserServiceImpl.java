package com.networth.userservice.service.impl;

import com.networth.userservice.config.properties.KeycloakProperties;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.exception.KeycloakException;
import com.networth.userservice.exception.UserNotFoundException;
import com.networth.userservice.mapper.UserMapper;
import com.networth.userservice.repository.UserRepository;
import com.networth.userservice.service.UserService;
import com.networth.userservice.util.HelperUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
    private final WebClient webClient;
    private final KeycloakProperties keycloakProperties;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, HelperUtils helperUtils, WebClient webClient, KeycloakProperties keycloakProperties) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.helperUtils = helperUtils;
        this.webClient = webClient;
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
    public Mono<Object> registerUser(RegisterDto registerDto) {

        log.info("Register User Flow Started");

        // Check if username already exists
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return Mono.error(new InvalidInputException("Username already in use: " + registerDto.getUsername()));
        }

        log.info("Username Passed Checks");

        // Check if email already exists
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return Mono.error(new InvalidInputException("Email already in use: " + registerDto.getEmail()));
        }

        log.info("Email Passed Checks");

        // Validate Password
        helperUtils.validatePassword(registerDto.getPassword());

        log.info("Password Passed Checks");

        // Get Admin Access Token
        return helperUtils.getAdminAccessToken().flatMap(accessToken -> {

            log.info("Creating User Representation");

            // Create User Representation
            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("username", registerDto.getUsername());
            userRepresentation.put("email", registerDto.getEmail());
            userRepresentation.put("enabled", true);

            log.info("Creating User Password Credential");

            // Set up Password
            Map<String, Object> passwordCredential = new HashMap<>();
            passwordCredential.put("type", "PASSWORD");
            passwordCredential.put("value", registerDto.getPassword());
            passwordCredential.put("temporary", false);

            userRepresentation.put("credentials", Collections.singletonList(passwordCredential));

            log.info("Adding User To Keycloak");

            // Create User in Keycloak
            return webClient.post()
                    .uri(keycloakProperties.getBaseUri()+ "/admin/realms/networth/users")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userRepresentation)
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(response -> {
                        if (response.getStatusCode() != HttpStatus.CREATED) {
                            log.error("Error response from Keycloak: Status Code: {}, Body: {}", response.getStatusCode(), response);
                            return Mono.error(new KeycloakException("Unable to create user in Keycloak. Status: " + response.getStatusCode()));
                        }

                        // Extract Keycloak user ID
                        log.info("Extracting Keycloak User Id");
                        String keycloakUserId = response.getHeaders().getLocation().getPath().split("/")[1];

                        if(keycloakUserId == null) {
                            log.error("Keycloak User Id is Null");
                            return Mono.error(new KeycloakException("Keycloak User Id is Null"));
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
                        return Mono.just(userRepository.save(user))
                                .map(userMapper::toUserOutput);
                    });
        });
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

package com.networth.userservice.controller;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UpdateUserDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided information. " +
            "The user's unique identifier is returned in the Location header of the response.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserOutput.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request - the provided data is invalid or incomplete"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions - the requester does not have permission to create a new user"),
            @ApiResponse(responseCode = "409", description = "Already Exists - a user with the provided details already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - an unexpected error occurred while processing the request")
    })
    public ResponseEntity<UserOutput> registerUser(@RequestBody RegisterDto registerDto, UriComponentsBuilder uriBuilder) {
        UserOutput userOutput = userService.registerUser(registerDto);

        URI location = uriBuilder
                .path("/{id}")
                .buildAndExpand(userOutput.getUserId())
                .toUri();

        return ResponseEntity.created(location).body(userOutput);
    }


    @Operation(summary = "Get a user by Keycloak ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the user"),
            @ApiResponse(responseCode = "400", description = "Invalid Keycloak ID supplied"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<UserOutput> getUser(@RequestHeader("X-User-ID") String keycloakUserId) {
        UserOutput userOutput = userService.getUser(keycloakUserId);
        return ResponseEntity.ok(userOutput);
    }

    @PutMapping
    @Operation(summary = "Update a user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated the user"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserOutput> updateUser(@RequestHeader("X-User-ID") String keycloakUserId, @RequestBody UpdateUserDto updateUserDto) {
        UserOutput userOutput = userService.updateUser(keycloakUserId, updateUserDto);
        return ResponseEntity.ok(userOutput);
    }



    @DeleteMapping
    @Operation(summary = "Delete a user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted the user"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-User-ID") String keycloakUserId) {
        userService.deleteUser(keycloakUserId);
        return ResponseEntity.noContent().build();
    }
}

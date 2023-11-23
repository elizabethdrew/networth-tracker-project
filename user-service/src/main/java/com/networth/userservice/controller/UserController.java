package com.networth.userservice.controller;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.exception.DuplicateException;
import com.networth.userservice.exception.InsufficientPermissionException;
import com.networth.userservice.exception.InvalidInputException;
import com.networth.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "409", description = "Already Exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserOutput> registerUser(@RequestBody RegisterDto registerDto, UriComponentsBuilder uriBuilder) {
        UserOutput userOutput = userService.registerUser(registerDto);

        URI location = uriBuilder.path("/api/v1/users/{id}")
                .buildAndExpand(userOutput.getUserId())
                .toUri();

        return ResponseEntity.created(location).body(userOutput);
    }


    @GetMapping("/{userId}")
    @Operation(summary = "Get a user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the user"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserOutput> getUser(@PathVariable Long userId) {
        UserOutput userOutput = userService.getUser(userId);
        return ResponseEntity.ok(userOutput);
    }

//    @PutMapping("/{userId}")
//    @Operation(summary = "Update a user by id")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Updated the user"),
//            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
//            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
//            @ApiResponse(responseCode = "404", description = "User not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<UserOutput> updateUser(@PathVariable Long userId, @RequestBody RegisterDto registerDto) {
//        UserOutput userOutput = userService.updateUser(userId, registerDto);
//        return ResponseEntity.ok(userOutput);
//    }
//
//    @DeleteMapping("/{userId}")
//    @Operation(summary = "Delete a user by id")
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "Deleted the user"),
//            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
//            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
//            @ApiResponse(responseCode = "404", description = "User not found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        userService.deleteUser(userId);
//        return ResponseEntity.noContent().build();
//    }
}

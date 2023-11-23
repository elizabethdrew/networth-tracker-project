package com.networth.userservice.controller;

import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    // User Login
    @PostMapping("/login")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticates a user by their username and password, returning an access token for subsequent API calls."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or missing login information"),
            @ApiResponse(responseCode = "403", description = "Authentication failed due to invalid credentials or locked account"),
            @ApiResponse(responseCode = "500", description = "Server error during authentication process")
    })
    public ResponseEntity<LoginResponse> userLogin(@RequestBody LoginDto loginDto) {
        LoginResponse loginResponse = authService.userLogin(loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    // User Logout

    @PostMapping("/logout")
    @Operation(
            summary = "Logout a user",
            description = "Logs out a user by invalidating their current access token and ending their session."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or missing logout information"),
            @ApiResponse(responseCode = "403", description = "User not logged in or lacks permission to log out"),
            @ApiResponse(responseCode = "500", description = "Server error during logout process")
    })
    public ResponseEntity<?> userLogout(LogoutDto logoutDto) {
        authService.userLogout(logoutDto);
        return ResponseEntity.ok().body("User logged out successfully");
    }

}

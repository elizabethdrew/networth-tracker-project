package com.networth.userservice.controller;

import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.PasswordUpdateDto;
import com.networth.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    @Operation(summary = "User Login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User Logged In"),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<LoginResponse> userLogin(@RequestBody LoginDto loginDto) {
        LoginResponse loginResponse = authService.userLogin(loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    // User Logout

    @PostMapping("/logout")
    @Operation(summary = "User Logout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User Logged Out"),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<?> userLogout(@RequestBody Map<String, String> logoutRequest) {
        String refreshToken = logoutRequest.get("refresh_token");
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh Token is required");
        }
        authService.userLogout(refreshToken);
        return ResponseEntity.ok().body("User logged out successfully");
    }

    // User Password Update
    @PostMapping("/update-password")
    @Operation(summary = "User Update Password With Keycloak")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User Logged Out"),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "403", description = "Insufficient Permissions"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    public ResponseEntity<?> updatePassword(@RequestHeader("X-SID") String sid,
                                            @RequestBody PasswordUpdateDto passwordUpdateDto) {

        authService.updateUserPassword(sid, passwordUpdateDto);
        return ResponseEntity.ok().body("Password updated successfully");
    }


    /* User Password Recovery
    Requires emails to be setup within Keycloak. Not priority, will add to jobs list.
    */
}

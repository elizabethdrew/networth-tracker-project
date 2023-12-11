package com.networth.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.LogoutDto;
import com.networth.userservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testUserLogin() throws Exception {
        LoginDto mockLoginDto = new LoginDto();
        mockLoginDto = new LoginDto();
        mockLoginDto.setUsername("seeduser");
        mockLoginDto.setPassword("Password123!");

        LoginResponse mockLoginResponse = new LoginResponse();
        mockLoginResponse = new LoginResponse();
        mockLoginResponse.setUserId(1L);

        when(authService.userLogin(mockLoginDto)).thenReturn(mockLoginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockLoginDto)))
                .andExpect(status().isOk());

        verify(authService).userLogin(any(LoginDto.class));
    }

    @Test
    public void testUserLogout() throws Exception {
        LogoutDto mockLogoutDto = new LogoutDto();
        mockLogoutDto.setAccessToken("mockAccessToken");
        mockLogoutDto.setIdTokenHint("mockIdTokenHint");
        mockLogoutDto.setRefreshToken("mockRefreshToken");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockLogoutDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged out successfully"));

        verify(authService).userLogout(any(LogoutDto.class));
    }
}

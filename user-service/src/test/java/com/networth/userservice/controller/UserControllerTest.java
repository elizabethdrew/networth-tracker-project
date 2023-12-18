package com.networth.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.TaxRate;
import com.networth.userservice.dto.UpdateUserDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        RegisterDto mockRegisterDto = new RegisterDto();
        mockRegisterDto = new RegisterDto();
        mockRegisterDto.setUsername("seeduser");
        mockRegisterDto.setEmail("seeduser@example.co.uk");
        mockRegisterDto.setPassword("Password123!");

        UserOutput mockUserOutput = new UserOutput();
        mockUserOutput = new UserOutput();
        mockUserOutput.setUserId(1L);
        mockUserOutput.setUsername("seeduser");
        mockUserOutput.setEmail("seeduser@example.co.uk");
        mockUserOutput.setDateOfBirth(null);
        mockUserOutput.setTaxRate(null);
        mockUserOutput.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        mockUserOutput.setDateUpdated(null);
        mockUserOutput.setActiveUser(true);

        when(userService.registerUser(any(RegisterDto.class))).thenReturn(mockUserOutput);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRegisterDto)))
                .andExpect(status().isCreated());

        verify(userService).registerUser(any(RegisterDto.class));
    }

    @Test
    public void testGetUser() throws Exception {
        Long keycloakUserId = 1L;
        UserOutput mockUserOutput = new UserOutput();
        mockUserOutput = new UserOutput();
        mockUserOutput.setUserId(keycloakUserId);
        mockUserOutput.setUsername("seeduser");
        mockUserOutput.setEmail("seeduser@example.co.uk");
        mockUserOutput.setDateOfBirth(null);
        mockUserOutput.setTaxRate(null);
        mockUserOutput.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        mockUserOutput.setDateUpdated(null);
        mockUserOutput.setActiveUser(true);

        when(userService.getUser(String.valueOf(keycloakUserId))).thenReturn(mockUserOutput);

        mockMvc.perform(get("/api/v1/users")
                        .header("X-User-ID", keycloakUserId))
                .andExpect(status().isOk());

        verify(userService).getUser(String.valueOf(keycloakUserId));
    }

    @Test
    public void testUpdateUser() throws Exception {
        Long keycloakUserId = 1L;
        UpdateUserDto mockUpdateUserDto = new UpdateUserDto();
        mockUpdateUserDto.setEmail("seeduser@example.co.uk");
        mockUpdateUserDto.setDateOfBirth(LocalDate.parse("1986-09-02"));
        mockUpdateUserDto.setTaxRate(TaxRate.valueOf("BASIC"));

        UserOutput mockUserOutput = new UserOutput();
        mockUserOutput = new UserOutput();
        mockUserOutput.setUserId(keycloakUserId);
        mockUserOutput.setUsername("seeduser");
        mockUserOutput.setEmail("seeduser@example.co.uk");
        mockUserOutput.setDateOfBirth(LocalDate.parse("1986-09-02"));
        mockUserOutput.setTaxRate(TaxRate.valueOf("BASIC"));
        mockUserOutput.setDateOpened(LocalDateTime.parse("2023-06-06T12:00:00"));
        mockUserOutput.setDateUpdated(LocalDateTime.parse("2023-06-07T12:00:00"));
        mockUserOutput.setActiveUser(true);

        when(userService.updateUser(eq(String.valueOf(keycloakUserId)), any(UpdateUserDto.class))).thenReturn(mockUserOutput);

        mockMvc.perform(put("/api/v1/users")
                        .header("X-User-ID", keycloakUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUpdateUserDto)))
                .andExpect(status().isOk());

        verify(userService).updateUser(eq(String.valueOf(keycloakUserId)), any(UpdateUserDto.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        String keycloakUserId = "user-id";

        mockMvc.perform(delete("/api/v1/users")
                        .header("X-User-ID", keycloakUserId))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(keycloakUserId);
    }

}


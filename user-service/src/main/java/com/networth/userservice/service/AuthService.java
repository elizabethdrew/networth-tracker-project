package com.networth.userservice.service;

import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.LogoutDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    LoginResponse userLogin(LoginDto loginDto);

    void userLogout(LogoutDto logoutDto);

}

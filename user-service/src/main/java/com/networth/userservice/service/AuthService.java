package com.networth.userservice.service;

import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.PasswordUpdateDto;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthService {
    LoginResponse userLogin(LoginDto loginDto);

    void userLogout(String refreshToken);

    void updateUserPassword(String sid, PasswordUpdateDto passwordUpdateDto);
}

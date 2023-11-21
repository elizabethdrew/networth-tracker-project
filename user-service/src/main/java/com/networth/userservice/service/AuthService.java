package com.networth.userservice.service;

import com.networth.userservice.dto.LoginDto;
import com.networth.userservice.dto.LoginResponse;

public interface AuthService {
    LoginResponse userLogin(LoginDto loginDto);

}

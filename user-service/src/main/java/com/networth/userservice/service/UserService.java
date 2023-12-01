package com.networth.userservice.service;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UpdateUserDto;
import com.networth.userservice.dto.UserOutput;

public interface UserService {
    UserOutput registerUser(RegisterDto registerDto);
    UserOutput getUser(String keycloakId);
    UserOutput updateUser(String keycloakId, UpdateUserDto updateUserDto);
    void deleteUser(String keycloakId);
}

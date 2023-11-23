package com.networth.userservice.service;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UserOutput;
import reactor.core.publisher.Mono;

public interface UserService {
    UserOutput registerUser(RegisterDto registerDto);
    UserOutput getUser(Long userId);

//    UserOutput updateUser(Long userId, RegisterDto registerDto);
//    void deleteUser(Long userId);
}

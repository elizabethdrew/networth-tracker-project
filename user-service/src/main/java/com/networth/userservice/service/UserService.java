package com.networth.userservice.service;

import com.networth.userservice.dto.UserInput;
import com.networth.userservice.dto.UserOutput;

public interface UserService {
    UserOutput createUser(UserInput userInput);
    UserOutput getUser(Long userId);
    UserOutput updateUser(Long userId, UserInput userInput);
    void deleteUser(Long userId);
}

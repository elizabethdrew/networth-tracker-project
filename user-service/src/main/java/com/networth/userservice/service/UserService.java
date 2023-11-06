package com.networth.userservice.service;

import com.networth.userservice.entity.User;

public interface UserService {
    User createUser(User user);
    User getUser(Long userId);
    User updateUser(Long userId, User updatedUser);

    void deleteUser(Long userId);
}

package com.drew.testservice.service;

import com.drew.testservice.entity.User;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUser(Long userId);
}

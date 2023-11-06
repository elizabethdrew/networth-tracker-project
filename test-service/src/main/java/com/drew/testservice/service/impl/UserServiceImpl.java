package com.drew.testservice.service.impl;

import com.drew.testservice.entity.User;
import com.drew.testservice.repository.UserRepository;
import com.drew.testservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return repository.findByUserId(userId);
    }
}

package com.networth.userservice.service.impl;

import com.networth.userservice.exception.ResourceNotFoundException;
import com.networth.userservice.entity.User;
import com.networth.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final com.networth.userservice.repository.UserRepository repository;

    @Autowired
    public UserServiceImpl(com.networth.userservice.repository.UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getUser(Long userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        return repository.findByUserId(userId)
                .map(user -> {
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return repository.save(user);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!repository.existsByUserId(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        repository.deleteById(userId);
    }

    @Override
    public User createUser(User user) {
        user.setPassword(user.getPassword());
        return repository.save(user);
    }
}

package com.networth.userservice.repository;

import com.networth.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(Long userId);

    Optional<User> findByUserId(Long userId);
}

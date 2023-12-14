package com.drew.accountservice.repository;

import com.drew.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByKeycloakId(String keycloakId);
    Optional<Account> findByIdAndKeycloakId(Long accountId, String keycloakUserId);
}

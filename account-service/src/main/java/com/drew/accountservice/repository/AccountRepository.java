package com.drew.accountservice.repository;

import com.drew.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

package com.drew.accountservice.repository;

import com.drew.accountservice.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long>, JpaSpecificationExecutor<Balance> {

    List<Balance> findAllByAccountId(Long accountId);
    Optional<Balance> findTopByAccountIdOrderByReconcileDateDesc(Long accountId);
}

package com.drew.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    private Long balanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "accountId", insertable = false, updatable = false)
    @JsonBackReference
    private Account account;

    @Column(name = "reconcile_date")
    private LocalDate reconcileDate;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "difference_from_last")
    private BigDecimal differenceFromLast;

    @Column(name = "deposit_value")
    private BigDecimal depositValue;

    @Column(name = "withdrawal_value")
    private BigDecimal withdrawalValue;

    @Column(name = "interest_value")
    private BigDecimal interestValue;

    @Column(name = "fees_value")
    private BigDecimal feesValue;

    @Column(name = "bonus_value")
    private BigDecimal bonusValue;

    @Column(name = "growth_value")
    private BigDecimal growthValue;

    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public LocalDate getReconcileDate() {
        return reconcileDate;
    }

    public void setReconcileDate(LocalDate reconcileDate) {
        this.reconcileDate = reconcileDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getDepositValue() {
        return depositValue;
    }

    public void setDepositValue(BigDecimal depositValue) {
        this.depositValue = depositValue;
    }

    public BigDecimal getWithdrawalValue() {
        return withdrawalValue;
    }

    public void setWithdrawalValue(BigDecimal withdrawalValue) {
        this.withdrawalValue = withdrawalValue;
    }

    public BigDecimal getInterestValue() {
        return interestValue;
    }

    public void setInterestValue(BigDecimal interestValue) {
        this.interestValue = interestValue;
    }

    public BigDecimal getFeesValue() {
        return feesValue;
    }

    public void setFeesValue(BigDecimal feesValue) {
        this.feesValue = feesValue;
    }

    public BigDecimal getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(BigDecimal bonusValue) {
        this.bonusValue = bonusValue;
    }

    public BigDecimal getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(BigDecimal growthValue) {
        this.growthValue = growthValue;
    }

    public BigDecimal getDifferenceFromLast() {
        return differenceFromLast;
    }

    public void setDifferenceFromLast(BigDecimal differenceFromLast) {
        this.differenceFromLast = differenceFromLast;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

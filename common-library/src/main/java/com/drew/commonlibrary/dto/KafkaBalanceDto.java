package com.drew.commonlibrary.dto;

import java.math.BigDecimal;

public class KafkaBalanceDto {

    private Long accountId;
    private BigDecimal currentBalance;
    private BigDecimal depositValue;
    private BigDecimal withdrawalValue;
    private BigDecimal interestValue;
    private BigDecimal feesValue;
    private BigDecimal bonusValue;
    private BigDecimal growthValue;

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
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

    public void setWithdrawalValue(BigDecimal withdrawlValue) {
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}


package com.drew.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BalanceAllocationDto {
    @JsonProperty("balance")
    private BigDecimal balance = BigDecimal.ZERO;
    @JsonProperty("deposit_value")
    private BigDecimal depositValue = BigDecimal.ZERO;
    @JsonProperty("withdrawal_value")
    private BigDecimal withdrawalValue = BigDecimal.ZERO;
    @JsonProperty("interest_value")
    private BigDecimal interestValue = BigDecimal.ZERO;
    @JsonProperty("fees_value")
    private BigDecimal feesValue = BigDecimal.ZERO;
    @JsonProperty("bonus_value")
    private BigDecimal bonusValue = BigDecimal.ZERO;
    @JsonProperty("growth_value")
    private BigDecimal growthValue = BigDecimal.ZERO;



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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}


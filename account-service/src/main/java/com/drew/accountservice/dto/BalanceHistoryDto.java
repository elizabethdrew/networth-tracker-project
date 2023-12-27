package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Balance;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BalanceHistoryDto {
    @JsonProperty("balances")
    private List<Balance> balances;

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

}

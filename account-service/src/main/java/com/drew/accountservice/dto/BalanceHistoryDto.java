package com.drew.accountservice.dto;

import com.drew.accountservice.entity.Balance;

import java.util.List;

public class BalanceHistoryDto {
    private List<Balance> balances;

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

}

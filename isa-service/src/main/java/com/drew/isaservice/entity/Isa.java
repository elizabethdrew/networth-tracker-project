package com.drew.isaservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "isa")
public class Isa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_id", nullable = false)
    private String keycloakId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_year")
    private TaxYear taxYear;

    @Column(name = "tax_year_max_isa_allowance")
    private BigDecimal taxYearMaxIsaAllowance;

    @Column(name = "cash_isa_account_id")
    private Long cashIsaAccountId;

    @Column(name = "cash_isa_paid_in_total")
    private BigDecimal cashIsaPainInTotal;

    @Column(name = "cash_isa_withdrawn_total")
    private BigDecimal cashIsaWithdrawnTotal;

    @Column(name = "cash_isa_balance")
    private BigDecimal cashIsaBalance;

    @Column(name = "stocks_shares_isa_account_id")
    private Long stocksSharesIsaAccountId;

    @Column(name = "stocks_shares_isa_paid_in_total")
    private BigDecimal stocksSharesIsaPainInTotal;

    @Column(name = "stocks_shares_isa_withdrawn_total")
    private BigDecimal stocksSharesIsaWithdrawnTotal;

    @Column(name = "stocks_shares_isa_balance")
    private BigDecimal stocksSharesIsaBalance;

    @Column(name = "innovative_finance_isa_account_id")
    private Long innovativeFinanceIsaAccountId;

    @Column(name = "innovative_finance_isa_paid_in_total")
    private BigDecimal innovativeFinanceIsaPainInTotal;

    @Column(name = "innovative_finance_isa_withdrawn_total")
    private BigDecimal innovativeFinanceIsaWithdrawnTotal;

    @Column(name = "innovative_finance_isa_balance")
    private BigDecimal innovativeFinanceIsaBalance;

    @Column(name = "lifetime_isa_account_id")
    private Long lifetimeIsaAccountId;

    @Column(name = "lifetime_isa_paid_in_total")
    private BigDecimal lifetimeIsaPainInTotal;

    @Column(name = "lifetime_isa_withdrawn_total")
    private BigDecimal lifetimeIsaWithdrawnTotal;

    @Column(name = "lifetime_isa_balance")
    private BigDecimal lifetimeIsaBalance;

    @Column(name = "tax_year_in")
    private BigDecimal taxYearIn;

    @Column(name = "tax_year_withdrawn")
    private BigDecimal taxYearWithdrawn;

    @Column(name = "tax_year_balance")
    private BigDecimal taxYearBalance;

    public enum TaxYear {
        YEAR_2020_2021("2020-2021"),
        YEAR_2021_2022("2021-2022"),
        YEAR_2022_2023("2022-2023"),
        YEAR_2023_2024("2023-2024"),
        YEAR_2024_2025("2024-2025");

        private final String yearString;

        TaxYear(String yearString) {
            this.yearString = yearString;
        }

        @Override
        public String toString() {
            return this.yearString;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public TaxYear getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(TaxYear taxYear) {
        this.taxYear = taxYear;
    }

    public BigDecimal getTaxYearMaxIsaAllowance() {
        return taxYearMaxIsaAllowance;
    }

    public void setTaxYearMaxIsaAllowance(BigDecimal taxYearMaxIsaAllowance) {
        this.taxYearMaxIsaAllowance = taxYearMaxIsaAllowance;
    }

    public Long getCashIsaAccountId() {
        return cashIsaAccountId;
    }

    public void setCashIsaAccountId(Long cashIsaAccountId) {
        this.cashIsaAccountId = cashIsaAccountId;
    }

    public BigDecimal getCashIsaPainInTotal() {
        return cashIsaPainInTotal;
    }

    public void setCashIsaPainInTotal(BigDecimal cashIsaPainInTotal) {
        this.cashIsaPainInTotal = cashIsaPainInTotal;
    }

    public BigDecimal getCashIsaWithdrawnTotal() {
        return cashIsaWithdrawnTotal;
    }

    public void setCashIsaWithdrawnTotal(BigDecimal cashIsaWithdrawnTotal) {
        this.cashIsaWithdrawnTotal = cashIsaWithdrawnTotal;
    }

    public BigDecimal getCashIsaBalance() {
        return cashIsaBalance;
    }

    public void setCashIsaBalance(BigDecimal cashIsaBalance) {
        this.cashIsaBalance = cashIsaBalance;
    }

    public Long getStocksSharesIsaAccountId() {
        return stocksSharesIsaAccountId;
    }

    public void setStocksSharesIsaAccountId(Long stocksSharesIsaAccountId) {
        this.stocksSharesIsaAccountId = stocksSharesIsaAccountId;
    }

    public BigDecimal getStocksSharesIsaPainInTotal() {
        return stocksSharesIsaPainInTotal;
    }

    public void setStocksSharesIsaPainInTotal(BigDecimal stocksSharesIsaPainInTotal) {
        this.stocksSharesIsaPainInTotal = stocksSharesIsaPainInTotal;
    }

    public BigDecimal getStocksSharesIsaWithdrawnTotal() {
        return stocksSharesIsaWithdrawnTotal;
    }

    public void setStocksSharesIsaWithdrawnTotal(BigDecimal stocksSharesIsaWithdrawnTotal) {
        this.stocksSharesIsaWithdrawnTotal = stocksSharesIsaWithdrawnTotal;
    }

    public BigDecimal getStocksSharesIsaBalance() {
        return stocksSharesIsaBalance;
    }

    public void setStocksSharesIsaBalance(BigDecimal stocksSharesIsaBalance) {
        this.stocksSharesIsaBalance = stocksSharesIsaBalance;
    }

    public Long getInnovativeFinanceIsaAccountId() {
        return innovativeFinanceIsaAccountId;
    }

    public void setInnovativeFinanceIsaAccountId(Long innovativeFinanceIsaAccountId) {
        this.innovativeFinanceIsaAccountId = innovativeFinanceIsaAccountId;
    }

    public BigDecimal getInnovativeFinanceIsaPainInTotal() {
        return innovativeFinanceIsaPainInTotal;
    }

    public void setInnovativeFinanceIsaPainInTotal(BigDecimal innovativeFinanceIsaPainInTotal) {
        this.innovativeFinanceIsaPainInTotal = innovativeFinanceIsaPainInTotal;
    }

    public BigDecimal getInnovativeFinanceIsaWithdrawnTotal() {
        return innovativeFinanceIsaWithdrawnTotal;
    }

    public void setInnovativeFinanceIsaWithdrawnTotal(BigDecimal innovativeFinanceIsaWithdrawnTotal) {
        this.innovativeFinanceIsaWithdrawnTotal = innovativeFinanceIsaWithdrawnTotal;
    }

    public BigDecimal getInnovativeFinanceIsaBalance() {
        return innovativeFinanceIsaBalance;
    }

    public void setInnovativeFinanceIsaBalance(BigDecimal innovativeFinanceIsaBalance) {
        this.innovativeFinanceIsaBalance = innovativeFinanceIsaBalance;
    }

    public Long getLifetimeIsaAccountId() {
        return lifetimeIsaAccountId;
    }

    public void setLifetimeIsaAccountId(Long lifetimeIsaAccountId) {
        this.lifetimeIsaAccountId = lifetimeIsaAccountId;
    }

    public BigDecimal getLifetimeIsaPainInTotal() {
        return lifetimeIsaPainInTotal;
    }

    public void setLifetimeIsaPainInTotal(BigDecimal lifetimeIsaPainInTotal) {
        this.lifetimeIsaPainInTotal = lifetimeIsaPainInTotal;
    }

    public BigDecimal getLifetimeIsaWithdrawnTotal() {
        return lifetimeIsaWithdrawnTotal;
    }

    public void setLifetimeIsaWithdrawnTotal(BigDecimal lifetimeIsaWithdrawnTotal) {
        this.lifetimeIsaWithdrawnTotal = lifetimeIsaWithdrawnTotal;
    }

    public BigDecimal getLifetimeIsaBalance() {
        return lifetimeIsaBalance;
    }

    public void setLifetimeIsaBalance(BigDecimal lifetimeIsaBalance) {
        this.lifetimeIsaBalance = lifetimeIsaBalance;
    }

    public BigDecimal getTaxYearIn() {
        return taxYearIn;
    }

    public void setTaxYearIn(BigDecimal taxYearIn) {
        this.taxYearIn = taxYearIn;
    }

    public BigDecimal getTaxYearWithdrawn() {
        return taxYearWithdrawn;
    }

    public void setTaxYearWithdrawn(BigDecimal taxYearWithdrawn) {
        this.taxYearWithdrawn = taxYearWithdrawn;
    }

    public BigDecimal getTaxYearBalance() {
        return taxYearBalance;
    }

    public void setTaxYearBalance(BigDecimal taxYearBalance) {
        this.taxYearBalance = taxYearBalance;
    }


}

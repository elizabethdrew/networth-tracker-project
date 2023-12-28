package com.drew.isaservice.entity;

import com.drew.commonlibrary.types.TaxYear;
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

    @Column(name = "tax_year_max_lisa_allowance")
    private BigDecimal taxYearMaxLisaAllowance;

    @Column(name = "cash_isa_account_id")
    private Long cashIsaAccountId;

    @Column(name = "cash_isa_paid_in_total")
    private BigDecimal cashIsaPaidInTotal;

    @Column(name = "cash_isa_withdrawn_total")
    private BigDecimal cashIsaWithdrawnTotal;

    @Column(name = "cash_isa_balance")
    private BigDecimal cashIsaBalance;

    @Column(name = "stocks_shares_isa_account_id")
    private Long stocksSharesIsaAccountId;

    @Column(name = "stocks_shares_isa_paid_in_total")
    private BigDecimal stocksSharesIsaPaidInTotal;

    @Column(name = "stocks_shares_isa_withdrawn_total")
    private BigDecimal stocksSharesIsaWithdrawnTotal;

    @Column(name = "stocks_shares_isa_balance")
    private BigDecimal stocksSharesIsaBalance;

    @Column(name = "innovative_finance_isa_account_id")
    private Long innovativeFinanceIsaAccountId;

    @Column(name = "innovative_finance_isa_paid_in_total")
    private BigDecimal innovativeFinanceIsaPaidInTotal;

    @Column(name = "innovative_finance_isa_withdrawn_total")
    private BigDecimal innovativeFinanceIsaWithdrawnTotal;

    @Column(name = "innovative_finance_isa_balance")
    private BigDecimal innovativeFinanceIsaBalance;

    @Column(name = "lifetime_isa_account_id")
    private Long lifetimeIsaAccountId;

    @Column(name = "lifetime_isa_paid_in_total")
    private BigDecimal lifetimeIsaPaidInTotal;

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

    public void setCashIsaBalance(BigDecimal cashIsaBalance) {
        this.cashIsaBalance = cashIsaBalance;
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

    public BigDecimal getCashIsaPaidInTotal() {
        return cashIsaPaidInTotal;
    }

    public void setCashIsaPaidInTotal(BigDecimal cashIsaPaidInTotal) {
        this.cashIsaPaidInTotal = cashIsaPaidInTotal;
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


    public Long getStocksSharesIsaAccountId() {
        return stocksSharesIsaAccountId;
    }

    public void setStocksSharesIsaAccountId(Long stocksSharesIsaAccountId) {
        this.stocksSharesIsaAccountId = stocksSharesIsaAccountId;
    }

    public BigDecimal getStocksSharesIsaPaidInTotal() {
        return stocksSharesIsaPaidInTotal;
    }

    public void setStocksSharesIsaPaidInTotal(BigDecimal stocksSharesIsaPaidInTotal) {
        this.stocksSharesIsaPaidInTotal = stocksSharesIsaPaidInTotal;
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

    public BigDecimal getInnovativeFinanceIsaPaidInTotal() {
        return innovativeFinanceIsaPaidInTotal;
    }

    public void setInnovativeFinanceIsaPaidInTotal(BigDecimal innovativeFinanceIsaPaidInTotal) {
        this.innovativeFinanceIsaPaidInTotal = innovativeFinanceIsaPaidInTotal;
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

    public BigDecimal getLifetimeIsaPaidInTotal() {
        return lifetimeIsaPaidInTotal;
    }

    public void setLifetimeIsaPaidInTotal(BigDecimal lifetimeIsaPaidInTotal) {
        this.lifetimeIsaPaidInTotal = lifetimeIsaPaidInTotal;
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


    public BigDecimal getTaxYearMaxLisaAllowance() {
        return taxYearMaxLisaAllowance;
    }

    public void setTaxYearMaxLisaAllowance(BigDecimal taxYearMaxLisaAllowance) {
        this.taxYearMaxLisaAllowance = taxYearMaxLisaAllowance;
    }
}

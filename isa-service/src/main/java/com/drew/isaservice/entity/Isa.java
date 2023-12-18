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

}

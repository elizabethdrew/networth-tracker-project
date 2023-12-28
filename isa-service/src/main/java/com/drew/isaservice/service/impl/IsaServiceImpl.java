package com.drew.isaservice.service.impl;

import com.drew.commonlibrary.dto.AccountIsaDto;
import com.drew.commonlibrary.dto.KafkaBalanceDto;
import com.drew.commonlibrary.types.TaxYear;
import com.drew.isaservice.entity.Isa;
import com.drew.isaservice.exception.IsaDepositException;
import com.drew.isaservice.exception.LisaDepositException;
import com.drew.isaservice.repository.IsaRepository;
import com.drew.isaservice.service.IsaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
public class IsaServiceImpl implements IsaService {

    private final IsaRepository isaRepository;
    TaxYear currentTaxYear = TaxYear.YEAR_2023_2024; // Should update automatically
    BigDecimal taxYearMaxIsaAllowance = BigDecimal.valueOf(20000); // Should update automatically
    BigDecimal taxYearMaxLisaAllowance = BigDecimal.valueOf(4000);

    public IsaServiceImpl(IsaRepository isaRepository) {
        this.isaRepository = isaRepository;
    }

    @Override
    public void addIsaAccountToIsaService(AccountIsaDto accountIsaDto) {
        log.info("Adding new ISA account: " + accountIsaDto);

        // Check if user has ISA log for this tax year
        Isa isaTracker = getOrCreateIsaTracker(accountIsaDto.keycloakId());

        // Update the relevant column based on account type
        switch (accountIsaDto.accountType()) {
            case CASH_ISA:
                if (isaTracker.getCashIsaAccountId() != null) {
                    log.error("Cash ISA account ID already exists for this tax year.");
                } else {
                    isaTracker.setCashIsaAccountId(accountIsaDto.accountId());
                }
                break;
            case STOCKS_AND_SHARES_ISA:
                if (isaTracker.getStocksSharesIsaAccountId() != null) {
                    log.error("Stocks & Shares ISA account ID already exists for this tax year.");
                } else {
                    isaTracker.setStocksSharesIsaAccountId(accountIsaDto.accountId());
                }
                break;
            case INNOVATIVE_FINANCE_ISA:
                if (isaTracker.getInnovativeFinanceIsaAccountId() != null) {
                    log.error("Innovative Finance ISA account ID already exists for this tax year.");
                } else {
                    isaTracker.setInnovativeFinanceIsaAccountId(accountIsaDto.accountId());
                }
                break;
            case LIFETIME_ISA:
                if (isaTracker.getLifetimeIsaAccountId() != null) {
                    log.error("Lifetime ISA account ID already exists for this tax year.");
                } else {
                    isaTracker.setLifetimeIsaAccountId(accountIsaDto.accountId());
                }
                break;
            default:
                log.error("Unknown ISA account type.");
                break;
        }

        // Save the updated ISA tracker
        isaRepository.save(isaTracker);
        log.info("Isa Tracker now updated");
    }

    @Override
    public void addIsaBalance(KafkaBalanceDto balance) {
        log.info("Adding new ISA balance: " + balance);

        Isa isaTracker = getOrCreateIsaTracker(balance.keycloakId());

        // Depending on account type, update different columns
        switch (balance.accountType()) {
            case CASH_ISA:
                updateCashIsaBalance(isaTracker, balance);
                break;
            case STOCKS_AND_SHARES_ISA:
                updateStocksAndSharesIsaBalance(isaTracker, balance);
                break;
            case INNOVATIVE_FINANCE_ISA:
                updateInnovativeFinanceIsaBalance(isaTracker, balance);
                break;
            case LIFETIME_ISA:
                updateLifetimeIsaBalance(isaTracker, balance);
                break;
            default:
                log.error("Unknown ISA account type.");
                break;
        }

        BigDecimal totalIsaBalance = calculateTotalIsaBalance(isaTracker);
        isaTracker.setTaxYearBalance(totalIsaBalance);

        // Save the updated ISA tracker
        isaRepository.save(isaTracker);
        log.info("Isa Tracker now updated, your balance is: " + isaTracker.getTaxYearBalance());
    }

    private Isa getOrCreateIsaTracker(String keycloakId) {
        return isaRepository.findByKeycloakIdAndTaxYear(keycloakId, currentTaxYear)
                .orElseGet(() -> {
                    Isa newIsaTracker = new Isa();
                    newIsaTracker.setKeycloakId(keycloakId);
                    newIsaTracker.setTaxYear(currentTaxYear);
                    newIsaTracker.setTaxYearMaxIsaAllowance(taxYearMaxIsaAllowance);
                    newIsaTracker.setTaxYearMaxLisaAllowance(taxYearMaxLisaAllowance);
                    return newIsaTracker;
                });
    }

    private void updateCashIsaBalance(Isa isaTracker, KafkaBalanceDto balance) {
        if (!Objects.equals(isaTracker.getCashIsaAccountId(), balance.accountId())) {
            log.error("A different Cash ISA has already been added this tax year");
            return;
        }

        // Withdrawals
        BigDecimal currentCashWithdrawal = isaTracker.getCashIsaWithdrawnTotal().add(balance.withdrawalValue());
        isaTracker.setCashIsaWithdrawnTotal(currentCashWithdrawal);
        BigDecimal totalWithdrawal = isaTracker.getTaxYearWithdrawn().add(balance.withdrawalValue());
        isaTracker.setTaxYearWithdrawn(totalWithdrawal);

        // Deposits
        BigDecimal currentCashDeposit = isaTracker.getCashIsaPaidInTotal().add(balance.depositValue());
        BigDecimal totalDeposited = isaTracker.getTaxYearIn().add(balance.depositValue());
        if (totalDeposited.compareTo(isaTracker.getTaxYearMaxIsaAllowance()) > 0) {
            throw new IsaDepositException("You have gone over your ISA tax year allowance");
        } else {
            isaTracker.setCashIsaPaidInTotal(currentCashDeposit);
            isaTracker.setTaxYearIn(totalDeposited);
        }

        // Balance
        BigDecimal currentCashBalance = isaTracker.getCashIsaBalance().add(balance.currentBalance());
        isaTracker.setCashIsaBalance(currentCashBalance);
    }

    private void updateStocksAndSharesIsaBalance(Isa isaTracker, KafkaBalanceDto balance) {
        if (!Objects.equals(isaTracker.getStocksSharesIsaAccountId(), balance.accountId())) {
            log.error("A different Stocks & Shares ISA has already been added this tax year");
            return;
        }

        // Withdrawals
        BigDecimal currentStocksSharesWithdrawal = isaTracker.getStocksSharesIsaWithdrawnTotal().add(balance.withdrawalValue());
        isaTracker.setStocksSharesIsaWithdrawnTotal(currentStocksSharesWithdrawal);
        BigDecimal totalWithdrawal = isaTracker.getTaxYearWithdrawn().add(balance.withdrawalValue());
        isaTracker.setTaxYearWithdrawn(totalWithdrawal);

        // Deposits
        BigDecimal currentStocksSharesDeposit = isaTracker.getStocksSharesIsaPaidInTotal().add(balance.depositValue());
        BigDecimal totalDeposited = isaTracker.getTaxYearIn().add(balance.depositValue());
        if (totalDeposited.compareTo(isaTracker.getTaxYearMaxIsaAllowance()) > 0) {
            throw new IsaDepositException("You have gone over your ISA tax year allowance");
        } else {
            isaTracker.setStocksSharesIsaPaidInTotal(currentStocksSharesDeposit);
            isaTracker.setTaxYearIn(totalDeposited);
        }

        // Balance
        BigDecimal currentStocksSharesBalance = isaTracker.getStocksSharesIsaBalance().add(balance.currentBalance());
        isaTracker.setStocksSharesIsaBalance(currentStocksSharesBalance);
    }

    private void updateInnovativeFinanceIsaBalance(Isa isaTracker, KafkaBalanceDto balance) {
        if (!Objects.equals(isaTracker.getInnovativeFinanceIsaAccountId(), balance.accountId())) {
            log.error("A different Innovative Finance ISA has already been added this tax year");
            return;
        }

        // Withdrawals
        BigDecimal currentInnovativeFinanceWithdrawal = isaTracker.getInnovativeFinanceIsaWithdrawnTotal().add(balance.withdrawalValue());
        isaTracker.setInnovativeFinanceIsaWithdrawnTotal(currentInnovativeFinanceWithdrawal);
        BigDecimal totalWithdrawal = isaTracker.getTaxYearWithdrawn().add(balance.withdrawalValue());
        isaTracker.setTaxYearWithdrawn(totalWithdrawal);

        // Deposits
        BigDecimal currentInnovativeFinanceDeposit = isaTracker.getInnovativeFinanceIsaPaidInTotal().add(balance.depositValue());
        BigDecimal totalDeposited = isaTracker.getTaxYearIn().add(balance.depositValue());
        if (totalDeposited.compareTo(isaTracker.getTaxYearMaxIsaAllowance()) > 0) {
            throw new IsaDepositException("You have gone over your ISA tax year allowance");
        } else {
            isaTracker.setInnovativeFinanceIsaPaidInTotal(currentInnovativeFinanceDeposit);
            isaTracker.setTaxYearIn(totalDeposited);
        }

        // Balance
        BigDecimal currentInnovativeFinanceBalance = isaTracker.getInnovativeFinanceIsaBalance().add(balance.currentBalance());
        isaTracker.setInnovativeFinanceIsaBalance(currentInnovativeFinanceBalance);
    }

    private void updateLifetimeIsaBalance(Isa isaTracker, KafkaBalanceDto balance) {
        if (!Objects.equals(isaTracker.getLifetimeIsaAccountId(), balance.accountId())) {
            log.error("A different Lifetime ISA has already been added this tax year");
            return;
        }

        // Withdrawals
        BigDecimal currentLifetimeWithdrawal = isaTracker.getLifetimeIsaWithdrawnTotal().add(balance.withdrawalValue());
        isaTracker.setLifetimeIsaWithdrawnTotal(currentLifetimeWithdrawal);
        BigDecimal totalWithdrawal = isaTracker.getTaxYearWithdrawn().add(balance.withdrawalValue());
        isaTracker.setTaxYearWithdrawn(totalWithdrawal);

        // Deposits
        BigDecimal currentLifetimeDeposit = isaTracker.getLifetimeIsaPaidInTotal().add(balance.depositValue());
        BigDecimal totalDeposited = isaTracker.getTaxYearIn().add(balance.depositValue());
        if (totalDeposited.compareTo(isaTracker.getTaxYearMaxIsaAllowance()) > 0) {
            throw new IsaDepositException("You have gone over your ISA tax year allowance");
        } else if (currentLifetimeDeposit.compareTo(isaTracker.getTaxYearMaxLisaAllowance()) > 0 ) {
            throw new LisaDepositException("You have gone over your LISA tax year allowance");
        } else {
            isaTracker.setLifetimeIsaPaidInTotal(currentLifetimeDeposit);
            isaTracker.setTaxYearIn(totalDeposited);
        }

        // Balance
        BigDecimal currentLifetimeBalance = isaTracker.getLifetimeIsaBalance().add(balance.currentBalance());
        isaTracker.setLifetimeIsaBalance(currentLifetimeBalance);
    }

    private BigDecimal calculateTotalIsaBalance(Isa isaTracker) {
        // Calculate total ISA balance across all types
        return isaTracker.getCashIsaBalance()
                .add(isaTracker.getLifetimeIsaBalance())
                .add(isaTracker.getInnovativeFinanceIsaBalance())
                .add(isaTracker.getStocksSharesIsaBalance());
    }
}

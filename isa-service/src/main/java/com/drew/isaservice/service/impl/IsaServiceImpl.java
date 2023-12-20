package com.drew.isaservice.service.impl;

import com.drew.commonlibrary.dto.AccountIsaDto;
import com.drew.isaservice.entity.Isa;
import com.drew.isaservice.repository.IsaRepository;
import com.drew.isaservice.service.IsaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class IsaServiceImpl implements IsaService {

    private final IsaRepository isaRepository;

    // Should update automatically
    Isa.TaxYear currentTaxYear = Isa.TaxYear.YEAR_2023_2024;
    BigDecimal taxYearMaxIsaAllowance = BigDecimal.valueOf(20000);

    public IsaServiceImpl(IsaRepository isaRepository) {
        this.isaRepository = isaRepository;
    }

    @Override
    public void addIsaAccountToIsaService(AccountIsaDto accountIsaDto) {
        log.info("Adding new ISA account: " + accountIsaDto);

        // Check if user has ISA log for this tax year
        Optional<Isa> isaLogOpt = isaRepository.findByKeycloakIdAndTaxYear(accountIsaDto.keycloakId(), currentTaxYear);

        // Use existing log or create a new one
        Isa isaTracker = isaLogOpt.orElseGet(() -> {
            Isa newIsa = new Isa();
            newIsa.setKeycloakId(accountIsaDto.keycloakId());
            newIsa.setTaxYear(currentTaxYear);
            newIsa.setTaxYearMaxIsaAllowance(taxYearMaxIsaAllowance);
            return newIsa;
        });

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
}

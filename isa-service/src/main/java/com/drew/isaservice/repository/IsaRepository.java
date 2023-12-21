package com.drew.isaservice.repository;

import com.drew.commonlibrary.types.TaxYear;
import com.drew.isaservice.entity.Isa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IsaRepository extends JpaRepository<Isa, Long> {
    Optional<Isa> findByKeycloakIdAndTaxYear(String keycloakId, TaxYear taxYear);
}

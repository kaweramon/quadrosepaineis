package com.quadrosepaineisapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quadrosepaineisapi.model.release.FinancialRelease;

public interface FinancialReleaseRepository extends JpaRepository<FinancialRelease, Long> {

}

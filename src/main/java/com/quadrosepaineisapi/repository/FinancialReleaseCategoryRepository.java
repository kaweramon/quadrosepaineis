package com.quadrosepaineisapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quadrosepaineisapi.model.release.FinancialReleaseCategory;

public interface FinancialReleaseCategoryRepository extends JpaRepository<FinancialReleaseCategory, Long> {

}

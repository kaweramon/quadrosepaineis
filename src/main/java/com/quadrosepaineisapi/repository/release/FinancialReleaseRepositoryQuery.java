package com.quadrosepaineisapi.repository.release;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.quadrosepaineisapi.model.release.FinancialRelease;
import com.quadrosepaineisapi.repository.filter.FinancialReleaseFilter;
import com.quadrosepaineisapi.repository.projection.FinancialReleaseResume;

public interface FinancialReleaseRepositoryQuery {

	public Page<FinancialRelease> filter(FinancialReleaseFilter financialReleaseFilter, Pageable pageable);
	
	public Page<FinancialReleaseResume> resume(FinancialReleaseFilter financialReleaseFilter, Pageable pageable);
}

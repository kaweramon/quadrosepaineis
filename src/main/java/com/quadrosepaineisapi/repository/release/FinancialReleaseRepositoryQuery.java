package com.quadrosepaineisapi.repository.release;

import java.util.List;

import com.quadrosepaineisapi.model.release.FinancialRelease;
import com.quadrosepaineisapi.repository.filter.FinancialReleaseFilter;

public interface FinancialReleaseRepositoryQuery {

	public List<FinancialRelease> filter(FinancialReleaseFilter financialReleaseFilter);
	
}

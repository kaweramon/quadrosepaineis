package com.quadrosepaineisapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.model.release.FinancialRelease;
import com.quadrosepaineisapi.model.release.FinancialReleaseCategory;
import com.quadrosepaineisapi.repository.FinancialReleaseCategoryRepository;
import com.quadrosepaineisapi.repository.FinancialReleaseRepository;
import com.quadrosepaineisapi.repository.ProductRepository;
import com.quadrosepaineisapi.service.exception.FinancialReleaseCategoryNotFoundException;
import com.quadrosepaineisapi.service.exception.ProductNotFoundOrInactiveException;

@Service
public class ReleaseService {

	@Autowired
	private FinancialReleaseRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private FinancialReleaseCategoryRepository financialReleaseCategoryRepository;
	
	public FinancialRelease create(FinancialRelease release) {
		
		if (release.getProduct() != null) {
			Product product = productRepository.findOne(release.getProduct().getId());
			if (product == null || !product.getIsActive())
				throw new ProductNotFoundOrInactiveException();
		}
		
		if (release.getCategory() != null) {
			FinancialReleaseCategory category = financialReleaseCategoryRepository
					.findOne(release.getCategory().getId());
			if(category == null)
				throw new FinancialReleaseCategoryNotFoundException();
		}
		
		release.setRegisterDate(LocalDate.now());
		return repository.save(release);
	}

	public List<FinancialRelease> list() {
		return repository.findAll();
	}

	public FinancialRelease view(Long id) {
		return repository.findOne(id);
	}
}

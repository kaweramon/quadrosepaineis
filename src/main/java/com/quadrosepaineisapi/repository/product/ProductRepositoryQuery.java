package com.quadrosepaineisapi.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.filter.ProductFilter;

public interface ProductRepositoryQuery {

	public Page<Product> filter(ProductFilter productFilter, Pageable pageable);
	
}

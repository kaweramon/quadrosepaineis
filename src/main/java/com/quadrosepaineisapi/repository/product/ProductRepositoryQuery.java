package com.quadrosepaineisapi.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.quadrosepaineisapi.product.Product;
import com.quadrosepaineisapi.repository.filter.ProductFilter;
import com.quadrosepaineisapi.repository.projection.ProductResume;

public interface ProductRepositoryQuery {

	public Page<Product> filter(ProductFilter productFilter, Pageable pageable);
	
	public Page<ProductResume> resume(ProductFilter productFilter, Pageable pageable);
}

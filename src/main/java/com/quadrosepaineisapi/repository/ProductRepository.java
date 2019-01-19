package com.quadrosepaineisapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.product.ProductRepositoryQuery;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQuery {

	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM product where is_active = true;")
	public Integer getProductsLength();
	
	@Query(nativeQuery = true, 
			value = "SELECT * FROM product WHERE is_active IS TRUE ORDER BY id DESC LIMIT 1;")
	public Product findLastProduct();
}

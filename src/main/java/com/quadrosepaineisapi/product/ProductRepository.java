package com.quadrosepaineisapi.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quadrosepaineisapi.product.Product;
import com.quadrosepaineisapi.repository.product.ProductRepositoryQuery;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQuery {

	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM product where is_active = true;")
	public Integer getProductsLength();
	
	@Query(nativeQuery = true, 
			value = "SELECT * FROM product WHERE is_active IS TRUE ORDER BY id DESC LIMIT 1;")
	public Product findLastProduct();

	Optional<Product> findById(Long id);
}

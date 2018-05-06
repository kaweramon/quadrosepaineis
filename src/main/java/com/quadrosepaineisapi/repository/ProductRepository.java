package com.quadrosepaineisapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.product.ProductRepositoryQuery;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQuery {

}

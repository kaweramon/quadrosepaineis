package com.quadrosepaineisapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	public Product update(Long id, Product product) {
		Product productSaved = getProductById(id);
		BeanUtils.copyProperties(product, productSaved, "id", "registerDate");
		return repository.save(productSaved);
	}

	public void updateIsActiveProperty(Long id, Boolean isActive) {
		Product productSaved = getProductById(id);
		productSaved.setIsActive(isActive);
		repository.save(productSaved);
	}
	
	private Product getProductById(Long id) {
		Product productSaved = repository.findOne(id);
		if (productSaved == null)
			throw new EmptyResultDataAccessException(1);
		return productSaved;
	}
}

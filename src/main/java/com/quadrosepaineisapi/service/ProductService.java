package com.quadrosepaineisapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.ProductRepository;
import com.quadrosepaineisapi.util.QuadrosePaineisServiceUtil;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private QuadrosePaineisServiceUtil serviceUtil;
	
	public Product update(Long id, Product product) {
		Product productSaved = serviceUtil.getProductById(id);
		BeanUtils.copyProperties(product, productSaved, "id", "registerDate");
		return repository.save(productSaved);
	}

	public void updateIsActiveProperty(Long id, Boolean isActive) {
		Product productSaved = serviceUtil.getProductById(id);
		productSaved.setIsActive(isActive);
		repository.save(productSaved);
	}
	
	public Product view(Long id) {
		return serviceUtil.getProductById(id);
	}
}

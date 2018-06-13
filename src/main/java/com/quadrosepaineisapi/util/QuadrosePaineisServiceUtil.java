package com.quadrosepaineisapi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.exceptionhandler.ResourceNotFoundException;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.ProductRepository;

@Service
public class QuadrosePaineisServiceUtil {

	@Autowired
	private ProductRepository prodRepository;
	
	public Product getProductById(Long id) {
		Product product = prodRepository.findOne(id);
		
		if (product == null)
			throw new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND);
		
		if (!product.getIsActive())
			throw new BadRequestException(ErrorMessages.PRODUCT_INACTIVE);
		
		return product;
	}
	
	
}

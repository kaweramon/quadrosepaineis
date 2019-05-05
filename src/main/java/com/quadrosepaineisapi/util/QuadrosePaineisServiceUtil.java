package com.quadrosepaineisapi.util;

import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.exceptionhandler.ResourceNotFoundException;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuadrosePaineisServiceUtil {

	private final ProductRepository prodRepository;
	
	public Product getProductById(Long id) {
		Product product = prodRepository.findOne(id);
		
		if (product == null)
			throw new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND);
		
		if (!product.getIsActive())
			throw new BadRequestException(ErrorMessages.PRODUCT_INACTIVE);
		
		return product;
	}
	
	
}

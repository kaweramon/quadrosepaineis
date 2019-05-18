package com.quadrosepaineisapi.util;

import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.exceptionhandler.ResourceNotFoundException;
import com.quadrosepaineisapi.product.Product;
import com.quadrosepaineisapi.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuadrosePaineisServiceUtil {

	private final ProductRepository prodRepository;
	
	public Product getProductById(Long id) {
		Product product = prodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));
		
		if (!product.getIsActive())
			throw new BadRequestException(ErrorMessages.PRODUCT_INACTIVE);
		
		return product;
	}
	
	
}

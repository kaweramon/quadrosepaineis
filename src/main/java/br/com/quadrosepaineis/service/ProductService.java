package br.com.quadrosepaineis.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.quadrosepaineis.domain.Product;

public interface ProductService {

	public Product create(Product product);
	
	public Product update(Integer productId, Product product);
	
	public List<Product> list();
	
	public void delete(Integer productId);
	
	public Product createPhoto(Integer productId, MultipartFile photo) throws IOException;
}

package br.com.quadrosepaineis.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.quadrosepaineis.domain.Product;
import br.com.quadrosepaineis.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repository;
	
	public Product create(Product product) {		
		return repository.save(product);
	}

	public Product update(Integer productId, Product product) {

		Product productDB = repository.findOne(productId);
		
		if (productDB != null) {
			productDB.setName(product.getName());
			productDB.setCategory(product.getCategory());
			productDB.setDescription(product.getDescription());
			productDB.setDiameter(product.getDiameter());
			productDB.setHeight(product.getHeight());
			productDB.setWidth(product.getWidth());
			repository.save(productDB);
		}
		
		return productDB;
	}

	public List<Product> list() {
		return (List<Product>) repository.findAll();
	}

	public void delete(Integer productId) {
		repository.delete(productId);
	}

	public Product createPhoto(Integer productId, MultipartFile photo) throws IOException {
		Product productDB = repository.findOne(productId);
		
		productDB.setPhoto(photo.getBytes());
		
		return repository.save(productDB);
	}

}

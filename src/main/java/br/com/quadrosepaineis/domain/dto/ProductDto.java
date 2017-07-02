package br.com.quadrosepaineis.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import br.com.quadrosepaineis.domain.Product;
import br.com.quadrosepaineis.domain.ProductCategory;
import lombok.Data;

@Data
public class ProductDto {

	private Integer id;
	private String name;
	private ProductCategory category;
	private String description;
	private Double height;
	private Double width;
	private Double diameter;
	private byte[] photo;
	
	public static ProductDto fromObject(Product product) {
		ProductDto productDto = new ProductDto();
		BeanUtils.copyProperties(product, productDto);
		return productDto;
	}
	
	public static List<ProductDto> fromObject(List<Product> listProducts) {
		List<ProductDto> listProductDto = new ArrayList<ProductDto>();
		for (Product product: listProducts) {
			listProductDto.add(fromObject(product));
		}
		return listProductDto;
	}
	
	public Product toObject() {
		Product product = new Product();
		BeanUtils.copyProperties(this, product);
		return product;
	}
}

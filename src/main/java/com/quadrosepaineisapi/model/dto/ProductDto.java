package com.quadrosepaineisapi.model.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quadrosepaineisapi.model.Category;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.model.ProductImgUrl;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {

	private Long id;
	@NotNull
	@Size(min = 3, max = 50)
	private String name;
	private Double price;
	private String description;
	private String registerDateStr;
	private List<Category> categories;
	private Double width;
	private Double height;
	private Double diameter;
	private Double weight;
	@NotNull
	private Boolean isActive;
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<ProductImgUrl> listProdImgUrls;
	
	public ProductDto(Long id, String name, String registerDateStr) {
		this.id = id;
		this.name = name;
		this.registerDateStr = registerDateStr;
	}
	
	public static Page<ProductToListDto> fromObject(Page<Product> products, Pageable pageable) {
		List<ProductToListDto> productsDto = new ArrayList<ProductToListDto>();
		for (Product product : products) {
			String registerDateStr = formatter.format(product.getRegisterDate());
			productsDto.add(new ProductToListDto(product.getId(), product.getName(), product.getPrice(),
					product.getDescription(), registerDateStr, product.getSequence(), product.getListImgUrl() != null && product.getListImgUrl().size() > 0 
					? product.getListImgUrl().get(0).getUrl() : null));
		}
		
		return new PageImpl<>(productsDto, pageable, products.getTotalPages());
	}
	
	
	public static ProductDto fromObject(Product product) {
		ProductDto productDto = new ProductDto();
		BeanUtils.copyProperties(product, productDto);
		if (product.getCategories() != null)
			productDto.setCategories(product.getCategories());
		if (product.getRegisterDate() != null)
			productDto.setRegisterDateStr(formatter.format(product.getRegisterDate()));
		if (product.getListImgUrl() != null)
			productDto.setListProdImgUrls(product.getListImgUrl());
		return productDto;
	}
	
	public Product toObject() {
		Product product = new Product();
		BeanUtils.copyProperties(this, product);
		if (this.categories != null)
			product.setCategories(this.categories);
		
		if (this.listProdImgUrls != null)
			product.setListImgUrl(this.listProdImgUrls);
		
		return product;
	}
	
	@Data
	public static class ProductToListDto {
		private Long id;
		private String name;
		private Double price;
		private String description;
		private String registerDateStr;
		private Integer sequence;
		private String mainPhotoUrl;
		
		public ProductToListDto(Long id, String name, Double price, String description, 
				String registerDateStr, Integer sequence, String mainPhotoUrl) {
			this.id = id;
			this.name = name;
			this.price = price;
			this.description = description;
			this.registerDateStr = registerDateStr;
			this.sequence = sequence;
			this.mainPhotoUrl = mainPhotoUrl;
		}
	}
	
}

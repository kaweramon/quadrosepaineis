package com.quadrosepaineisapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductImage {

	private int id;
	
	private String miniUrl;
	private String smallUrl;
	private String productUrl;
	private String largeUrl;
}

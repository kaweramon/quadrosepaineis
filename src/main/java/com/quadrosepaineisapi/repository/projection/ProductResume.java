package com.quadrosepaineisapi.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResume {

	private Long id;
	private String name;
	private Double price;
	private String description;
	private Integer sequence;
	
}

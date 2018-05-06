package com.quadrosepaineisapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity(name = "category")
@Data
public class Category {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 20)
	private String name;
	
	/*@ManyToMany(mappedBy = "categories")
	private List<Product> products;*/
	
}

package com.quadrosepaineisapi.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity(name = "product")
@Data
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 50)
	private String name;
	
	private String description;
	
	@Column(name = "register_date")
	private LocalDateTime registerDate;
	
	@ManyToMany
	@JoinTable(name = "product_categories", joinColumns = 
		{@JoinColumn (name = "id_product", referencedColumnName = "id")}, inverseJoinColumns = 
		{@JoinColumn (name = "id_category", referencedColumnName = "id")})
	private List<Category> categories;
	
	private Double width;
	
	private Double height;
	
	private Double diameter;
	
	private Double weight;
	
	@NotNull
	private Boolean isActive;
	
}

package com.quadrosepaineisapi.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity(name = "product")
@Data
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 50)
	@Column(unique = true)
	private String name;
	
	@NotNull
	private Double price;
	
	private String description;
	
	@Column(name = "register_date")
	private LocalDateTime registerDate;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "product_categories", joinColumns = 
		{@JoinColumn (name = "id_product", referencedColumnName = "id")}, inverseJoinColumns = 
		{@JoinColumn (name = "id_category", referencedColumnName = "id")})
	private List<Category> categories;
	
	private Double width;
	
	private Double height;
	
	private Double diameter;
	
	private Double weight;
	
	private Boolean isActive;
	
	private Integer sequence;
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "product_img_url", joinColumns = 
			{@JoinColumn(name = "id_product", referencedColumnName = "id")}, inverseJoinColumns = 
			{@JoinColumn(name = "id_img_url", referencedColumnName = "id")})
	private List<ProductImgUrl> listImgUrl;

}

package br.com.quadrosepaineis.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private ProductCategory category;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	private Double height;
	
	private Double width;
	
	private Double diameter;
	
	@Lob
	private byte[] photo;
}

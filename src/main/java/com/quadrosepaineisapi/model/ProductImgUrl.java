package com.quadrosepaineisapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "img_url")
@Data()
@NoArgsConstructor()
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductImgUrl {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 255)
	@Column(unique = true)
	private String url;
	
	private Boolean isActive;
	
	public ProductImgUrl(String url) {
		this.url = url;
		this.isActive = true;
	}
}

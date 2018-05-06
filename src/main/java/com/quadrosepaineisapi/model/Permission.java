package com.quadrosepaineisapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import com.sun.istack.internal.NotNull;

import lombok.Data;

@Data
@Entity
public class Permission {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 50)
	private String description;
}

package com.quadrosepaineisapi.repository.filter;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ProductFilter {

	private String name;
	private String description;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime registerDateFrom;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime registerDateTo;
	
}

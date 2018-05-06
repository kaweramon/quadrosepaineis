package com.quadrosepaineisapi.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FinancialReleaseFilter {

	private String description;
	private String obs;
	private BigDecimal value;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate dueDateFrom;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate dueDateTo;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate payDateFrom;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate payDateTo;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate registerDateFrom;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate registerDateTo;
}

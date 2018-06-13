package com.quadrosepaineisapi.repository.projection;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialReleaseResume {

	private Long id;
	private String description;
	private String obs;
	private BigDecimal value;
	
}

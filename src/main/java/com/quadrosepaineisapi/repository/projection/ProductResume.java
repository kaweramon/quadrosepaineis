package com.quadrosepaineisapi.repository.projection;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResume {

	private Long id;
	private String name;
	private String description;
	private LocalDateTime registerDate;
	private byte[] photo;
	
}

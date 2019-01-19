package com.quadrosepaineisapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quadrosepaineisapi.model.Category;
import com.quadrosepaineisapi.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = false)
	public Category update(Category category, Long id) {
		Category categorySaved = findById(id);
		BeanUtils.copyProperties(category, categorySaved, "id");
		
		return repository.save(categorySaved);
	}
	
	@Transactional(readOnly = true)
	public Category findById(Long id) {
		Category category = repository.findOne(id);
		
		if (category == null)
			throw new EmptyResultDataAccessException(1);
		
		return category;
	}
	
}

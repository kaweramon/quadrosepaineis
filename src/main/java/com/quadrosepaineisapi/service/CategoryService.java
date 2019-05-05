package com.quadrosepaineisapi.service;

import com.quadrosepaineisapi.model.Category;
import com.quadrosepaineisapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository repository;

	public void create(Category category) {
		repository.save(category);
	}

	public Category update(Category category, Long id) {
		Category categorySaved = findById(id);
		BeanUtils.copyProperties(category, categorySaved, "id");
		
		return repository.save(categorySaved);
	}
	
	@Transactional(readOnly = true)
	public Category findById(Long id) {
		Category category = repository.findOne(id);
		
		if (category == null)
			throw new EntityNotFoundException("Categoria não encontrada!");
		
		return category;
	}
	
}

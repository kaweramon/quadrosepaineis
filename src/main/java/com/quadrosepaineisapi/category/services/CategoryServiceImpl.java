package com.quadrosepaineisapi.category.services;

import com.quadrosepaineisapi.category.Category;
import com.quadrosepaineisapi.category.CategoryRepository;
import com.quadrosepaineisapi.exceptionhandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository repository;

	public void create(Category category) {
		repository.save(category);
	}

	public Category update(Category category, Long id) {
		Category categorySaved = findById(id);
		BeanUtils.copyProperties(category, categorySaved, "id");
		
		return repository.save(categorySaved);
	}
	
	public Category findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada!"));
	}
	
}

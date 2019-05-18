package com.quadrosepaineisapi.category.v1;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.quadrosepaineisapi.category.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.quadrosepaineisapi.event.ResourceCreatedEvent;
import com.quadrosepaineisapi.category.Category;
import com.quadrosepaineisapi.category.CategoryRepository;
import com.quadrosepaineisapi.category.services.CategoryServiceImpl;
import com.quadrosepaineisapi.util.UrlConstants;

@RestController
@RequestMapping(UrlConstants.URL_CATEGORIES)
public class CategoryResourceV1 {

	@Autowired
	private CategoryRepository repository;
	
	// disparar evento
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private CategoryServiceImpl service;
	
	@GetMapping
	public List<Category> list() {
		return repository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Category> save(@RequestBody @Valid @NotNull CategoryDTO categoryDTO,
										 HttpServletResponse response) {
		Category categorySaved = repository.save(categoryDTO.to());
		
		publisher.publishEvent(new ResourceCreatedEvent(this, response, categorySaved.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categorySaved);
	}
	
	@PutMapping(UrlConstants.PARAM_ID)
	public ResponseEntity<Category> update(@RequestBody @Valid @NotNull CategoryDTO categoryDTO, @PathVariable("id") Long id,
			HttpServletResponse response) {
		Category categorySaved = service.update(categoryDTO.to(), id);
		publisher.publishEvent(new ResourceCreatedEvent(this, response, categorySaved.getId()));
		return ResponseEntity.ok(categorySaved);
	}
	
	@GetMapping(UrlConstants.PARAM_ID)
	@ResponseBody
	public ResponseEntity<CategoryDTO> view(@PathVariable("id") Long id) {
		return ResponseEntity.ok(CategoryDTO.from(service.findById(id)));
	}
	
	@DeleteMapping(UrlConstants.PARAM_ID)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		repository.delete(id);
	}
}

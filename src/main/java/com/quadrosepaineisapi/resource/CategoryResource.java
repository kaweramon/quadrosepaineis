package com.quadrosepaineisapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.quadrosepaineisapi.event.ResourceCreatedEvent;
import com.quadrosepaineisapi.model.Category;
import com.quadrosepaineisapi.repository.CategoryRepository;
import com.quadrosepaineisapi.service.CategoryService;
import com.quadrosepaineisapi.util.UrlConstants;

@RestController
@RequestMapping(UrlConstants.URL_CATEGORIES)
public class CategoryResource {

	@Autowired
	private CategoryRepository repository;
	
	// disparar evento
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public List<Category> list() {
		return repository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Category> save(@RequestBody @Valid Category category, HttpServletResponse response) {
		Category categorySaved = repository.save(category);
		
		publisher.publishEvent(new ResourceCreatedEvent(this, response, categorySaved.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categorySaved);
	}
	
	@PutMapping(UrlConstants.PARAM_ID)
	public ResponseEntity<Category> update(@RequestBody @Valid Category category, @PathVariable("id") Long id, 
			HttpServletResponse response) {
		Category categorySaved = service.update(category, id);
		publisher.publishEvent(new ResourceCreatedEvent(this, response, categorySaved.getId()));
		return ResponseEntity.ok(categorySaved);
	}
	
	@GetMapping(UrlConstants.PARAM_ID)
	@ResponseBody
	public ResponseEntity<Category> view(@PathVariable("id") Long id) {
		Category category = repository.findOne(id);
		if (category != null)
			return ResponseEntity.ok().body(category);
		
		return ResponseEntity.notFound().build();
	}
	
}

package com.quadrosepaineisapi.resource;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quadrosepaineisapi.event.ResourceCreatedEvent;
import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.model.dto.ProductDto;
import com.quadrosepaineisapi.model.dto.ProductDto.ProductToListDto;
import com.quadrosepaineisapi.repository.ProductRepository;
import com.quadrosepaineisapi.repository.filter.ProductFilter;
import com.quadrosepaineisapi.repository.projection.ProductResume;
import com.quadrosepaineisapi.service.ProductService;
import com.quadrosepaineisapi.util.UrlConstants;

@RestController
@RequestMapping(UrlConstants.URL_PRODUCTS)
public class ProductResource {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private ProductService service;
	
//	@PreAuthorize("hasAuthority('CREATE_PRODUCT')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<ProductDto> create(@RequestBody @Valid Product product, 
			HttpServletResponse response) {
		product.setRegisterDate(LocalDateTime.now());
		product.setIsActive(true);
		Product productCreated = repository.save(product);
		
		publisher.publishEvent(new ResourceCreatedEvent(this, response, productCreated.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(ProductDto.fromObject(productCreated));
	}
	
//	@PreAuthorize("hasAuthority('RETRIEVE_PRODUCT')")
	@GetMapping
	public Page<ProductToListDto> search(ProductFilter productFilter,@PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
		return ProductDto.fromObject(repository.filter(productFilter, pageable), pageable);
	}
	
//	@PreAuthorize("hasAuthority('RETRIEVE_PRODUCT')")
	@GetMapping(path = "/resume")
	public Page<ProductResume> searchResume(ProductFilter productFilter, Pageable pageable) {
		return repository.resume(productFilter, pageable);
	}
	
//	@PreAuthorize("hasAuthority('RETRIEVE_PRODUCT')")
	@GetMapping(path = UrlConstants.PARAM_ID)
	@ResponseBody
	public ResponseEntity<ProductDto> view(@PathVariable("id") Long id) {
		return ResponseEntity.ok(ProductDto.fromObject(service.view(id)));
	}
	
	@PutMapping(UrlConstants.PARAM_ID)
//	@PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
	public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
		return ResponseEntity.ok(service.update(id, productDto.toObject()));
	}
	
	@PutMapping(UrlConstants.PARAM_ID + UrlConstants.URL_UPLOAD)
//	@PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
	@ResponseStatus(HttpStatus.OK)
	public void uploadImage(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
		if (photo != null) {
			
			if ((photo.getSize() / 1014) > 1)
				throw new BadRequestException("Foto não deve ser maior que 1MB");
			
			Product product = repository.findOne(id);
			
			if (product != null)
				try {
					product.setPhoto(photo.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			repository.save(product);
		}
	}
	
	@PutMapping(UrlConstants.PARAM_ID + UrlConstants.URL_IS_ACTIVE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
//	@PreAuthorize("hasAuthority('DELETE_PRODUCT')")
	public void updateIsActiveProperty(@PathVariable Long id, @RequestBody Boolean isActive) {
		service.updateIsActiveProperty(id, isActive);
	}
	
	@DeleteMapping(UrlConstants.PARAM_ID)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('DELETE_PRODUCT')")
	public void delete(@PathVariable("id") Long id) {
		repository.delete(id);
	}
	
}

package com.quadrosepaineisapi.product.v1;

import com.quadrosepaineisapi.event.ResourceCreatedEvent;
import com.quadrosepaineisapi.product.Product;
import com.quadrosepaineisapi.product.dto.ProductDto;
import com.quadrosepaineisapi.product.dto.ProductDto.ProductToListDto;
import com.quadrosepaineisapi.product.ProductRepository;
import com.quadrosepaineisapi.repository.filter.ProductFilter;
import com.quadrosepaineisapi.repository.projection.ProductResume;
import com.quadrosepaineisapi.product.services.ProductServiceImpl;
import com.quadrosepaineisapi.util.UrlConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.quadrosepaineisapi.util.UrlConstants.URL_UPLOAD_TO_MINIO;

@RestController
@RequestMapping(UrlConstants.URL_PRODUCTS)
@RequiredArgsConstructor
public class ProductControllerV1 {

	private final ProductRepository repository;
	
	private final ApplicationEventPublisher publisher;
	
	private final ProductServiceImpl service;

	@PreAuthorize("hasAuthority('CREATE_PRODUCT')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductDto productDto,
			HttpServletResponse response) {
		Product productCreated = service.create(productDto.toObject());
		
		publisher.publishEvent(new ResourceCreatedEvent(this, response, productCreated.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(ProductDto.fromObject(productCreated));
	}
	
	@PreAuthorize("hasAuthority('RETRIEVE_PRODUCT')")
	@GetMapping
	public Page<ProductToListDto> search(ProductFilter productFilter,@PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
		return ProductDto.fromObject(repository.filter(productFilter, pageable), pageable);
	}
	
	@PreAuthorize("hasAuthority('RETRIEVE_PRODUCT')")
	@GetMapping(path = "/resume")
	public Page<ProductResume> searchResume(ProductFilter productFilter, Pageable pageable) {
		return repository.resume(productFilter, pageable);
	}
	
	@PreAuthorize("hasAuthority('RETRIEVE_PRODUCT')")
	@GetMapping(path = UrlConstants.PARAM_ID)
	@ResponseBody
	public ResponseEntity<ProductDto> view(@PathVariable("id") Long id) {
		return ResponseEntity.ok(ProductDto.fromObject(service.view(id)));
	}
	
	@PutMapping(UrlConstants.PARAM_ID)
	@PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
	public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
		return ResponseEntity.ok(service.update(id, productDto.toObject()));
	}
	
	@PutMapping(UrlConstants.PARAM_ID + UrlConstants.URL_UPLOAD)
	@PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
	@ResponseStatus(HttpStatus.OK)
	public void uploadImage(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
		service.uploadImage(id, photo);
	}

	@PutMapping(UrlConstants.PARAM_ID + URL_UPLOAD_TO_MINIO)
	@PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
	@ResponseStatus(HttpStatus.OK)
	public void uploadToMinio(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
		service.uploadToMinio(id, photo);
	}

	@PutMapping(UrlConstants.PARAM_ID + UrlConstants.URL_IS_ACTIVE)
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
	public void updateIsActiveProperty(@PathVariable Long id, @RequestBody Boolean isActive) {
		service.updateIsActiveProperty(id, isActive);
	}
	
	@PutMapping(UrlConstants.URL_SEQUENCE)
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
	public void updateListSequence(@RequestBody List<Product> products) {
		service.updateSequence(products);
	}
	
	@DeleteMapping(UrlConstants.PARAM_ID)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('DELETE_PRODUCT')")
	public void delete(@PathVariable("id") Long id) {
		repository.delete(id);
	}
	
	@PutMapping(UrlConstants.PARAM_ID + UrlConstants.URL_UPLOAD_GALLERY)
	public void uploadGallery(@PathVariable Long id, @RequestBody List<MultipartFile> gallery) {
		service.uploadGallery(id, gallery);
	}
	
	@PutMapping(UrlConstants.PARAM_ID + UrlConstants.URL_UPDATE_GALLERY)
	public void updateGallery(@PathVariable Long id, @RequestBody List<MultipartFile> galleryToUpdate, 
			@RequestParam List<Long> listProductImgDeleted) {
		service.updateGallery(id, galleryToUpdate, listProductImgDeleted);
	}
	
}

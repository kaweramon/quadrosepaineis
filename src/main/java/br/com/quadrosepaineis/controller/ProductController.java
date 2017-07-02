package br.com.quadrosepaineis.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import br.com.quadrosepaineis.domain.dto.ProductDto;
import br.com.quadrosepaineis.service.ProductService;

@Controller
@RequestMapping(path = "/product")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody ProductDto create(@RequestBody ProductDto productDto) {
		return ProductDto.fromObject(service.create(productDto.toObject()));				
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{productId}")
	public @ResponseBody ProductDto update(@RequestParam("productId") Integer productId, @RequestBody ProductDto productDto) {
		return ProductDto.fromObject(service.create(productDto.toObject()));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<ProductDto> list(){
		return ProductDto.fromObject(service.list());
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/{productId}")
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@RequestParam("productId") Integer productId) {
		service.delete(productId);
	}
	
	@RequestMapping(path = "/photo/{productId}", method = RequestMethod.POST)
	public @ResponseBody ProductDto createPhoto(@RequestParam("productId") Integer productId, 
			@RequestParam("photo") MultipartFile photo) throws IOException {
		return ProductDto.fromObject(service.createPhoto(productId, photo));
	}
}

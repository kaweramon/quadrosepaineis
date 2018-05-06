package com.quadrosepaineisapi.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quadrosepaineisapi.event.ResourceCreatedEvent;
import com.quadrosepaineisapi.exceptionhandler.QuadrosePaineisExceptionHandler.ErrorMessage;
import com.quadrosepaineisapi.model.release.FinancialRelease;
import com.quadrosepaineisapi.service.ReleaseService;
import com.quadrosepaineisapi.service.exception.FinancialReleaseCategoryNotFoundException;
import com.quadrosepaineisapi.service.exception.ProductNotFoundOrInactiveException;
import com.quadrosepaineisapi.util.UrlConstants;;

@RestController
@RequestMapping(UrlConstants.URL_RELEASES)
public class FinancialReleaseResource {
	
	@Autowired
	private ReleaseService service;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@PostMapping
	public ResponseEntity<FinancialRelease> create(@Valid @RequestBody FinancialRelease release, HttpServletResponse response) {
		FinancialRelease releaseCreated = service.create(release);
		this.publisher.publishEvent(new ResourceCreatedEvent(this, response, releaseCreated.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(releaseCreated);
	}

	@GetMapping
	public List<FinancialRelease> list() {
		return service.list();
	}
	
	@GetMapping(UrlConstants.PARAM_ID)
	public ResponseEntity<FinancialRelease> view(@PathVariable Long id) {
		FinancialRelease release = service.view(id);
		return release != null ? ResponseEntity.ok(release) : ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler({ProductNotFoundOrInactiveException.class})
	public ResponseEntity<Object> handleProductNotFoundOrInactiveException(ProductNotFoundOrInactiveException ex) {
		String msgUser = messageSource.getMessage("product.not-found-or-inactive", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> errors = Arrays.asList(new ErrorMessage(msgUser, ex.toString()));
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler({FinancialReleaseCategoryNotFoundException.class})
	public ResponseEntity<Object> handleFinancialReleaseCategoryNotFoundException(FinancialReleaseCategoryNotFoundException ex) {
		String msgUser = messageSource.getMessage("financialRelease.not-found", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> errors = Arrays.asList(new ErrorMessage(msgUser, ex.toString()));
		return ResponseEntity.badRequest().body(errors);
	}
}

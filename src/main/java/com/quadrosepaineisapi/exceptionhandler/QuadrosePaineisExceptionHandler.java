package com.quadrosepaineisapi.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.Data;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class QuadrosePaineisExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String	invalidMessage = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> listErrors = Arrays.asList(new ErrorMessage(invalidMessage, 
				ex.getCause() != null ? ex.getCause().toString() : ex.toString()));
		return handleExceptionInternal(ex, listErrors, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<ErrorMessage> listErrors = getListErrors(ex.getBindingResult());
		return handleExceptionInternal(ex, listErrors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, 
			WebRequest request) {
		
		String resourceNotFound = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> listErrors = Arrays.asList(new ErrorMessage(resourceNotFound, ex.toString()));
		return handleExceptionInternal(ex, listErrors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
		String resourceNotAllowed = "";
		String rootCauseException = ExceptionUtils.getRootCauseMessage(ex);
		if (rootCauseException.contains("Duplicate entry"))
			resourceNotAllowed = "Já existe um registro com o valor " + StringUtils.substringBetween(rootCauseException, "'");
		else
			resourceNotAllowed = messageSource.getMessage("resource.not-allowed", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> listErrors = Arrays.asList(new ErrorMessage(resourceNotAllowed, 
				rootCauseException));
		return handleExceptionInternal(ex, listErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({MultipartException.class})
	public ResponseEntity<Object> handleMultipartException(MultipartException ex, WebRequest request) {
		String uploadMaxSize = messageSource.getMessage("upload.max-size", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> listErrors = Arrays.asList(new ErrorMessage(uploadMaxSize, 
				ExceptionUtils.getRootCauseMessage(ex)));
		return handleExceptionInternal(ex, listErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({SizeLimitExceededException.class})
	public ResponseEntity<Object> handleSizeLimitExceededException(SizeLimitExceededException ex, WebRequest request) {
		String uploadMaxSize = messageSource.getMessage("upload.max-size", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> listErrors = Arrays.asList(new ErrorMessage(uploadMaxSize, 
				ExceptionUtils.getRootCauseMessage(ex)));
		return handleExceptionInternal(ex, listErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({MaxUploadSizeExceededException.class})
	public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request) {
		String uploadMaxSize = messageSource.getMessage("upload.max-size", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> listErrors = Arrays.asList(new ErrorMessage(uploadMaxSize, 
				ExceptionUtils.getRootCauseMessage(ex)));
		return handleExceptionInternal(ex, listErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
		String uploadMaxSize = messageSource.getMessage("upload.max-size", null, LocaleContextHolder.getLocale());
		List<ErrorMessage> listErrors = Arrays.asList(new ErrorMessage(uploadMaxSize,
				ExceptionUtils.getRootCauseMessage(ex)));
		return handleExceptionInternal(ex, listErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	private List<ErrorMessage> getListErrors(BindingResult bindingResult) {
		List<ErrorMessage> listErrors = new ArrayList<ErrorMessage>();
		
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String msgUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			listErrors.add(new ErrorMessage(msgUser, fieldError.toString()));
		}
		
		return listErrors;
	}
	
	@Data
	public static class ErrorMessage {
		
		private String msgUser;
		private String msgDev;
		
		public ErrorMessage(String msgUser, String msgDev) {
			this.msgUser = msgUser;
			this.msgDev = msgDev;
		}
		
	}
	
}



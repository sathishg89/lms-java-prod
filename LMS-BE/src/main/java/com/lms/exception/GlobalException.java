package com.lms.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.lms.dto.CustomDto;
import com.lms.dto.EmailNotFoundDto;
import com.lms.dto.NameFoundDto;
import com.lms.exception.details.CustomException;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.exception.details.NameFoundException;

@RestControllerAdvice
@RequestMapping(produces = "application/json")
@ResponseBody
public class GlobalException {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

	@ExceptionHandler(NameFoundException.class)
	public ResponseEntity<NameFoundDto> nameFound(NameFoundException nf, WebRequest wr) {

		NameFoundDto nfd = new NameFoundDto();
		nfd.setErrorMessage(nf.getErrorMessage());

		return new ResponseEntity<NameFoundDto>(nfd, HttpStatus.FOUND);
	}

	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<EmailNotFoundDto> emailNotFound(EmailNotFoundException enf, WebRequest wr) {

		EmailNotFoundDto enfd = new EmailNotFoundDto();
		enfd.setErrorMessage(enf.getErrorMessage());
		enfd.setErrorCode(enf.getErrorCode());

		return new ResponseEntity<EmailNotFoundDto>(enfd, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomDto> custom(CustomException ce, WebRequest wr) {

		CustomDto cd = new CustomDto();

		cd.setErrorMessage(ce.getErrorMessage());
		cd.setErrorCode(ce.getErrorCode());

		return new ResponseEntity<CustomDto>(cd, HttpStatus.BAD_REQUEST);
	}

}

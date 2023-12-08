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
	public ResponseEntity<?> nameFound(NameFoundException nf, WebRequest wr) {

		NameFoundDto d = new NameFoundDto();
		d.setError("100");
		d.setMessage("User Already Registered");

		return new ResponseEntity<Object>(d, HttpStatus.FOUND);
	}

	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<?> emailNotFound(EmailNotFoundException enf, WebRequest wr) {

		EmailNotFoundDto efd = new EmailNotFoundDto();
		efd.setError("504");
		efd.setMessage(enf.getMessage());

		return new ResponseEntity<Object>(efd, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> custom(CustomException ce, WebRequest wr) {

		CustomDto cd = new CustomDto(ce.getMessage(), ce.getErrorcode());
		return new ResponseEntity<Object>(cd, HttpStatus.BAD_REQUEST);
	}

}

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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.lms.dto.CustomDto;
import com.lms.exception.details.CustomException;

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

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomDto> custom(CustomException ce, WebRequest wr) {

		CustomDto cd = new CustomDto();

		cd.setErrorMessage(ce.getErrorMessage());
		cd.setErrorCode(ce.getErrorCode());

		return new ResponseEntity<CustomDto>(cd, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File Size Exceeds The Allowed Limit 6MB ");
	}
}

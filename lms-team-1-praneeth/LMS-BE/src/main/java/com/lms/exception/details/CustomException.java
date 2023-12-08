package com.lms.exception.details;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;
	private String errorcode;

	public CustomException(String message, String errorcode) {

		this.errorcode = errorcode;
		this.message = message;
	}

	public CustomException(String message) {
		super(message);
	}

}

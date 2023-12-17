package com.lms.exception.details;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private String errorCode;

	public CustomException(String errorMessage, String errorCode) {

		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

}

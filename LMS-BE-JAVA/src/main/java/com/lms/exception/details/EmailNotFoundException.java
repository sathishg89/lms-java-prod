package com.lms.exception.details;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private String errorCode;

	public EmailNotFoundException(String errorMessage, String errorCode) {

		this.errorMessage = errorMessage;
		this.errorCode = errorCode;

	}

}

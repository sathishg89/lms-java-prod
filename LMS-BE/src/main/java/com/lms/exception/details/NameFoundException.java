package com.lms.exception.details;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public NameFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

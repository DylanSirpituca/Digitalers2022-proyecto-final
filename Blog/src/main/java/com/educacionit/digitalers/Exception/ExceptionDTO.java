package com.educacionit.digitalers.Exception;

public class ExceptionDTO extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExceptionDTO(String message) {
		super(message);
	}
}

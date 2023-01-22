package com.example.flabcaloriecounter.exception;

public class InvalidUserException extends RuntimeException {

	private final String message;

	public InvalidUserException(final String format, final String message) {
		super(format);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}

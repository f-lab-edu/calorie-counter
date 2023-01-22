package com.example.flabcaloriecounter.exception;

public class PasswordNotMatchException extends RuntimeException {
	private final String message;

	public PasswordNotMatchException(final String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}

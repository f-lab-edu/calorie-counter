package com.example.flabcaloriecounter.exception;

public class InvalidTokenException extends RuntimeException {
	private final String message;

	public InvalidTokenException(final String message) {
		super(message);
		this.message = message;
	}

	public InvalidTokenException(final String message, final Throwable e) {
		super(message, e);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getError() {
		return "invalid_token";
	}
}

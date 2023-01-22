package com.example.flabcaloriecounter.exception;

public class InvalidTokenRequestException extends RuntimeException {
	private final String message;

	public InvalidTokenRequestException(final String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public String getError() {
		return "invalid_request";
	}
}

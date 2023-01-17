package com.example.flabcaloriecounter.exception;

public class EmptyFeedException extends RuntimeException {
	private final String message;

	public EmptyFeedException(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}

package com.example.flabcaloriecounter.exception;

public class FeedNotFoundException extends RuntimeException {
	private final String message;

	public FeedNotFoundException(final String format, final String message) {
		super(format);
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}

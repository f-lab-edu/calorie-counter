package com.example.flabcaloriecounter.exception;

public class HasDuplicatedIdException extends RuntimeException {
	private final String message;

	public HasDuplicatedIdException(final String format, final String message) {
		super(format);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}

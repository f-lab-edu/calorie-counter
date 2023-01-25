package com.example.flabcaloriecounter.exception;

public class CommentNotFoundException extends RuntimeException {
	private final String message;

	public CommentNotFoundException(final String format, final String message) {
		super(format);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}

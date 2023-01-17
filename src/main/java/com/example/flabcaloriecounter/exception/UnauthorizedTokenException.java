package com.example.flabcaloriecounter.exception;

import io.jsonwebtoken.JwtException;

public class UnauthorizedTokenException extends RuntimeException {
	private final String message;

	public UnauthorizedTokenException(final String message) {
		super(message);
		this.message = message;
	}

	public UnauthorizedTokenException(final String message, final JwtException e) {
		super(message, e.getCause());
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}

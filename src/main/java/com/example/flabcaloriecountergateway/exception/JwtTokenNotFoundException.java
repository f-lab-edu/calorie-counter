package com.example.flabcaloriecountergateway.exception;

public class JwtTokenNotFoundException extends RuntimeException {
	public JwtTokenNotFoundException() {
	}

	public JwtTokenNotFoundException(String message) {
		super(message);
	}

	public JwtTokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

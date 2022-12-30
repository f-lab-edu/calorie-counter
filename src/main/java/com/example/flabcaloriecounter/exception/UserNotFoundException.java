package com.example.flabcaloriecounter.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(final String format) {
		super(format);
	}
}

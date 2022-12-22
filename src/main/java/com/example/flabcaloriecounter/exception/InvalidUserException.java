package com.example.flabcaloriecounter.exception;

public class InvalidUserException extends RuntimeException {

	public InvalidUserException(final String format) {
		super(format);
	}
}

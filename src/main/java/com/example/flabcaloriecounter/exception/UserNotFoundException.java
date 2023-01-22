package com.example.flabcaloriecounter.exception;

public class UserNotFoundException extends RuntimeException {

	private final String message = "존재하지 않는 유저입니다.";

	public UserNotFoundException(final String format) {
		super(format);
	}

	public String getMessage() {
		return this.message;
	}
}

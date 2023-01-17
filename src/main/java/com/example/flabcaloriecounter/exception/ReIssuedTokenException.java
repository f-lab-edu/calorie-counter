package com.example.flabcaloriecounter.exception;

import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseToken;

public class ReIssuedTokenException extends RuntimeException {
	private final ResponseToken responseToken;
	private final String message;

	public ReIssuedTokenException(String message, ResponseToken responseToken) {
		this.message = message;
		this.responseToken = responseToken;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public ResponseToken getResponseToken() {
		return responseToken;
	}
}

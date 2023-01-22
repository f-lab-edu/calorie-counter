package com.example.flabcaloriecounter.exception;

import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;

public class ReIssuedTokenException extends RuntimeException {
	private final ResponseIssuedToken responseIssuedToken;
	private final String message;

	public ReIssuedTokenException(String message, ResponseIssuedToken responseIssuedToken) {
		this.message = message;
		this.responseIssuedToken = responseIssuedToken;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public ResponseIssuedToken getResponseToken() {
		return responseIssuedToken;
	}
}

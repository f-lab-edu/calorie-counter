package com.example.flabcaloriecounter.user.application.port.in.response;

import org.springframework.http.HttpStatus;

public record ResponseTokenDto(
	String message,
	String accessToken,
	String refreshToken,
	HttpStatus statusCode
) {
}

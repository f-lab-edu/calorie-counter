package com.example.flabcaloriecounter.user.application.port.in.dto;

public record ResponseIssuedToken(
	String accessToken,
	String tokenType,
	long expiresIn
) {
}

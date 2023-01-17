package com.example.flabcaloriecounter.user.application.port.in.dto;

public record ResponseToken(
	String accessToken,
	String refreshToken
) {
}

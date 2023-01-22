package com.example.flabcaloriecounter.user.application.port.in.dto;

import org.springframework.http.HttpStatus;

public record ResponseTokenAuthFail(
	String error,
	String message,
	HttpStatus statusCode
) {

}

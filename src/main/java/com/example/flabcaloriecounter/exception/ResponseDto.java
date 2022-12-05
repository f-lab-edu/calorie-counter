package com.example.flabcaloriecounter.exception;

import org.springframework.http.HttpStatus;

public record ResponseDto(String message, HttpStatus statusCode) {

}

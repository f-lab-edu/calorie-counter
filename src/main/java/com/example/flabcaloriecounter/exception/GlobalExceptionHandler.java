package com.example.flabcaloriecounter.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.flabcaloriecounter.util.CustomResponse;
import com.example.flabcaloriecounter.util.StatusEnum;

/**
 * 실패시 응답
 * result : "error"
 * message : message
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomResponse<?>> methodArgumentNotValidExHandler() {
		return CustomResponse.error(StatusEnum.INVALID_FORM_INPUT.getMessage(),
			StatusEnum.INVALID_FORM_INPUT.getHttpStatus());
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomResponse<?>> customExceptionHandler(CustomException e) {
		return CustomResponse.error(e.getStatusEnum().getMessage(), e.getStatusEnum().getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomResponse<?>> exceptionHandler() {
		return CustomResponse.error(StatusEnum.ERROR.getMessage(), StatusEnum.ERROR.getHttpStatus());
	}
}

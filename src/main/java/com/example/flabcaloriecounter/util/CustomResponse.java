package com.example.flabcaloriecounter.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인값은 응답에 보이지않게한다.
public record CustomResponse<T>(
	String result,
	String errorMessage,
	T info
) {

	public static final String SUCCESS = "succeed";
	public static final String ERROR = "error";

	public CustomResponse(String result, String errorMessage) {
		this(result, errorMessage, null);
	}

	public CustomResponse(String result) {
		this(result, null, null);
	}

	public CustomResponse(String result, T info) {
		this(result, null, info);
	}

	public static <T> ResponseEntity<CustomResponse<T>> ok() {
		return new ResponseEntity<>(success(), HttpStatus.OK);
	}

	public static <T> ResponseEntity<CustomResponse<T>> ok(final T info) {
		return new ResponseEntity<>(success(info), HttpStatus.OK);
	}

	public static <T> ResponseEntity<CustomResponse<T>> created() {
		return new ResponseEntity<>(success(), HttpStatus.CREATED);
	}

	public static <T> ResponseEntity<CustomResponse<T>> created(final T info) {
		return new ResponseEntity<>(success(info), HttpStatus.CREATED);
	}

	public static ResponseEntity<CustomResponse<?>> error(final String message, final HttpStatus httpStatus) {
		return new ResponseEntity<>(new CustomResponse<>(ERROR, message), httpStatus);
	}

	private static <T> CustomResponse<T> success() {
		return new CustomResponse<>(SUCCESS);
	}

	private static <T> CustomResponse<T> success(final T info) {
		return new CustomResponse<>(SUCCESS, info);
	}
}

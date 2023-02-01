package com.example.flabcaloriecounter.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인값은 응답에 보이지않게한다.
public record CustomResponse<T>(
	String statusCode,
	T info
) {

	public static <T> CustomResponse<T> success() {
		return new CustomResponse<>("SUCCESS", null);
	}

	public static <T> CustomResponse<T> success(final T info) {
		return new CustomResponse<>("SUCCESS", info);
	}

	public static <T> CustomResponse<T> error(final String statusCode) {
		return new CustomResponse<>(statusCode, null);
	}
}

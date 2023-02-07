package com.example.flabcaloriecounter.exception;

import com.example.flabcaloriecounter.util.StatusEnum;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final StatusEnum statusEnum;
	private final String message;

	public CustomException(final StatusEnum statusEnum, final String message) {
		this.statusEnum = statusEnum;
		this.message = message;
	}

	public CustomException(final StatusEnum statusEnum, final Throwable e) {
		super(statusEnum.getMessage(), e);
		this.statusEnum = statusEnum;
		this.message = null;
	}

	public CustomException(final StatusEnum statusEnum) {
		this.statusEnum = statusEnum;
		this.message = null;
	}

	@Override
	public String getMessage() {
		if (message == null) {
			return this.statusEnum.getMessage();
		}
		return String.format("%s. %s", this.statusEnum.getMessage(), this.message);
	}
}

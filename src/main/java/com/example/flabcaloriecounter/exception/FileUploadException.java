package com.example.flabcaloriecounter.exception;

public class FileUploadException extends RuntimeException {
	private final String message;

	public FileUploadException(final String message, final Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}

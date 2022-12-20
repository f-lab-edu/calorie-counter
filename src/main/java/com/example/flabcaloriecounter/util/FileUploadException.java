package com.example.flabcaloriecounter.util;

public class FileUploadException extends RuntimeException {

	public static final String MESSAGE = "파일을 업로드 할 수 없습니다";

	public FileUploadException(Throwable cause) {
		super(MESSAGE, cause);
	}
}

package com.example.flabcaloriecounter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	public static final String HAS_DUPLICATED_ID_MSG = "이미 존재하는 ID 입니다";
	public static final String ARGUMENT_NOT_VALID_MSG = "올바르지 않은 입력값입니다.";
	public static final String EMPTY_FEED_MSG = "피드 내용이 비어있습니다";
	public static final String FILE_STORE_FAIL_MSG = "파일 저장에 실패했습니다.";

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(HasDuplicatedIdException.class)
	public ResponseDto hasDuplicatedIdExHandler() {
		return new ResponseDto(HAS_DUPLICATED_ID_MSG, HttpStatus.CONFLICT);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseDto methodArgumentNotValidExHandler() {
		return new ResponseDto(ARGUMENT_NOT_VALID_MSG, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseDto EmptyFeedWriteExHandler() {
		return new ResponseDto(EMPTY_FEED_MSG, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(FileUploadException.class)
	public ResponseDto FileUploadExHandler() {
		return new ResponseDto(FILE_STORE_FAIL_MSG, HttpStatus.BAD_REQUEST);
	}

	//todo RuntimeException
}

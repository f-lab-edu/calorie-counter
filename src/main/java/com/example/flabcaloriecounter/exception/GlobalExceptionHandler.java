package com.example.flabcaloriecounter.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	public static final String HAS_DUPLICATED_ID_MSG = "이미 존재하는 ID 입니다";
	public static final String ARGUMENT_NOT_VALID_MSG = "올바르지 않은 입력값입니다.";
	public static final String EMPTY_FEED_MSG = "피드 내용이 비어있습니다";
	public static final String FILE_STORE_FAIL_MSG = "파일 저장에 실패했습니다.";
	private static final String NOT_EXIST_USER_MSG = "존재하지 않는 유저입니다.";
	private static final String NOT_EXIST_FEED_MSG = "존재하지 않는 피드입니다.";
	private static final String FORBIDDEN_USER_MSG = "해당 권한이 없습니다";
	public static final String CURSOR_ERROR_MSG = "2이상의 cursor 위치를 입력해야합니다.";

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
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseDto emptyFeedWriteExHandler() {
		return new ResponseDto(EMPTY_FEED_MSG, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(FileUploadException.class)
	public ResponseDto fileUploadExHandler() {
		return new ResponseDto(FILE_STORE_FAIL_MSG, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseDto userNotFoundExHandler() {
		return new ResponseDto(NOT_EXIST_USER_MSG, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(FeedNotFoundException.class)
	public ResponseDto feedNotFoundExHandler() {
		return new ResponseDto(NOT_EXIST_FEED_MSG, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InvalidUserException.class)
	public ResponseDto forbiddenUserExHandler() {
		return new ResponseDto(FORBIDDEN_USER_MSG, HttpStatus.FORBIDDEN);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseDto bindExHandler() {
		return new ResponseDto(CURSOR_ERROR_MSG, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public ResponseDto runtimeExHandler() {
		return new ResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

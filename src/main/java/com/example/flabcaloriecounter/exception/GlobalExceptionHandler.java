package com.example.flabcaloriecounter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseTokenAuthFail;

@RestControllerAdvice
public class GlobalExceptionHandler {

	public static final String ARGUMENT_NOT_VALID_MSG = "올바르지 않은 폼 입력값입니다.";

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(HasDuplicatedIdException.class)
	public ResponseDto hasDuplicatedIdExHandler(HasDuplicatedIdException e) {
		return new ResponseDto(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseDto methodArgumentNotValidExHandler() {
		return new ResponseDto(ARGUMENT_NOT_VALID_MSG, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(EmptyFeedException.class)
	public ResponseDto emptyFeedWriteExHandler(EmptyFeedException e) {
		return new ResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(FileUploadException.class)
	public ResponseDto fileUploadExHandler(FileUploadException fileUploadException) {
		return new ResponseDto(fileUploadException.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseDto userNotFoundExHandler(UserNotFoundException userNotFoundException) {
		return new ResponseDto(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PasswordNotMatchException.class)
	public ResponseDto passwordNotMatchExHandler(PasswordNotMatchException passwordNotMatchException) {
		return new ResponseDto(passwordNotMatchException.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(FeedNotFoundException.class)
	public ResponseDto feedNotFoundExHandler(FeedNotFoundException feedNotFoundException) {
		return new ResponseDto(feedNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(InvalidUserException.class)
	public ResponseDto forbiddenUserExHandler(InvalidUserException invalidUserException) {
		return new ResponseDto(invalidUserException.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public ResponseDto runtimeExHandler() {
		return new ResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidTokenRequestException.class)
	public ResponseTokenAuthFail unAuthorizedHandler(InvalidTokenRequestException tokenException) {
		return new ResponseTokenAuthFail(tokenException.getError(), tokenException.getMessage(),
			HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseTokenAuthFail unAuthorizedHandler2(InvalidTokenException tokenException) {
		return new ResponseTokenAuthFail(tokenException.getError(), tokenException.getMessage(),
			HttpStatus.UNAUTHORIZED);
	}
}

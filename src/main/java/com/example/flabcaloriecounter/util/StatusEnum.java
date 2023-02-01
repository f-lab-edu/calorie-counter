package com.example.flabcaloriecounter.util;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum StatusEnum {

	USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 유저입니다"),
	FEED_NOT_FOUND(NOT_FOUND, "존재하지 않는 피드입니다"),
	INVALID_USER(FORBIDDEN, "권한이 없는 유저입니다"),
	PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
	DUPLICATED_ID(CONFLICT, "이미 존재하는 ID 입니다"),
	COMMENT_NOT_FOUND(NOT_FOUND, "부모댓글이 존재하지 않습니다"),
	EMPTY_FEED(BAD_REQUEST, "피드 내용이 비어있습니다"),
	INVALID_TOKEN(BAD_REQUEST, "토큰이 만료되었습니다. 다시 인증해주세요", "invalid_token"),
	ERROR(INTERNAL_SERVER_ERROR, "Internal server error"),
	FAIL_DECRYPTION(BAD_REQUEST, "토큰 복호화에 실패했습니다", "invalid_token"),
	INVALID_TOKEN_CONTENTS(NOT_FOUND, "token의 userId가 유효하지않습니다", "invalid_token"),
	INVALID_TOKEN_FORM(BAD_REQUEST, "요청헤더의 형식이 잘못되었습니다", "invalid_request"),
	EMPTY_TOKEN(BAD_REQUEST, "요청헤더에 토큰이 없습니다", "invalid_request"),
	FILE_PATH_NOT_FOUND(BAD_REQUEST, "해당 경로의 파일을 식별할수 없습니다"),
	INVALID_FORM_INPUT(BAD_REQUEST, "올바르지 않은 폼 입력값입니다."),
	FILE_SAVE_FAIL(BAD_REQUEST, "파일 저장에 실패했습니다");

	private final HttpStatus httpStatus;
	private final String message;
	private String errorCode;

	StatusEnum(final HttpStatus httpStatus, final String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	StatusEnum(final HttpStatus httpStatus, final String message, final String errorCode) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.errorCode = errorCode;
	}
}

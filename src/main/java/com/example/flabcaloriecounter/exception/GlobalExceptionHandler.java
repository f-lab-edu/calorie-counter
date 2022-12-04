package com.example.flabcaloriecounter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String HAS_DUPLICATED_ID_MSG = "이미 존재하는 ID 입니다";
    public static final String ARGUMENT_NOT_VALID_MSG = "올바르지 않은 입력값입니다.";

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
}

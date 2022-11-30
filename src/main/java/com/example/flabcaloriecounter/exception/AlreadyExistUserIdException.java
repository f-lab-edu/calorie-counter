package com.example.flabcaloriecounter.exception;

public class AlreadyExistUserIdException extends RuntimeException{
    public AlreadyExistUserIdException(String message) {
        super(message);
    }
}

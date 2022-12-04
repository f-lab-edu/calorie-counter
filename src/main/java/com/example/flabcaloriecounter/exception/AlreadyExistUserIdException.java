package com.example.flabcaloriecounter.exception;

public class AlreadyExistUserIdException extends RuntimeException {

    public AlreadyExistUserIdException(final String message) {
        super(message);
    }
}

package com.example.flabcaloriecounter.exception;

public interface ExceptionFunction<T, R> {
	R apply(T r) throws Exception;
}

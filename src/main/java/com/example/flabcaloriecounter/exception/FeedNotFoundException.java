package com.example.flabcaloriecounter.exception;

public class FeedNotFoundException extends RuntimeException {

	public FeedNotFoundException(final String format) {
		super(format);
	}
}

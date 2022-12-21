package com.example.flabcaloriecounter.feed.domain;

import java.time.LocalDateTime;

public record Feed(
	long id,
	String contents,
	LocalDateTime writeDate,
	long userId
) {
	public Feed(long id, String contents, long userId) {
		this(id, contents, null, userId);
	}
}

package com.example.flabcaloriecounter.feed.domain;

import java.time.LocalDateTime;

public record Comment(
	long id,
	long feedId,
	LocalDateTime writeDate,
	String contents,
	Long parentId,
	long userId
) {
	public Comment(long id, long feedId, LocalDateTime writeDate, String contents, long userId) {
		this(id, feedId, writeDate, contents, null, userId);
	}

	public Comment(long id, long feedId, LocalDateTime writeDate, String contents, Long parentId, long userId) {
		this.id = id;
		this.feedId = feedId;
		this.writeDate = writeDate;
		this.contents = contents;
		this.parentId = parentId;
		this.userId = userId;
	}
}

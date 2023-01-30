package com.example.flabcaloriecounter.feed.domain;

import java.time.LocalDateTime;

public record Comment(
	long id,
	long feedId,
	LocalDateTime writeDate,
	String contents,
	Long parentId,
	int depth,
	long userId,
	int groupNumber

) {
	public Comment(long id, long feedId, LocalDateTime writeDate, String contents, Long parentId, int depth,
		long userId, int groupNumber) {
		this.id = id;
		this.feedId = feedId;
		this.writeDate = writeDate;
		this.contents = contents;
		this.parentId = parentId;
		this.depth = depth;
		this.userId = userId;
		this.groupNumber = groupNumber;
	}
}

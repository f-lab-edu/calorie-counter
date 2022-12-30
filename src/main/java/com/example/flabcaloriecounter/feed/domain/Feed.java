package com.example.flabcaloriecounter.feed.domain;

import java.time.LocalDateTime;
import java.util.List;

public record Feed(
	long id,
	String contents,
	LocalDateTime writeDate,
	long userId,
	List<Photo> photos
) {
	//Mybatis FeedMapper.xml 매핑을 위한 생성자들
	public Feed(long id, String contents, long userId) {
		this(id, contents, null, userId, null);
	}

	public Feed(long id, String contents, LocalDateTime writeDate, long userId) {
		this(id, contents, writeDate, userId, null);
	}
}

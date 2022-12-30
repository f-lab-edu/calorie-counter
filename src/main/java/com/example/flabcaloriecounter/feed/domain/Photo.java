package com.example.flabcaloriecounter.feed.domain;

import java.time.LocalDateTime;

public record Photo(
	long id,
	String name,
	LocalDateTime uploadDate,
	String path,
	long feedId
) {

}

package com.example.flabcaloriecounter.feed.domain;

import java.time.LocalDateTime;

public record Photo(
	long id,
	Feed feed,
	LocalDateTime uploadDate,
	String path
) {

}

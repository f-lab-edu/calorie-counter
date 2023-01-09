package com.example.flabcaloriecounter.feed.application.port.in.dto;

import java.time.LocalDateTime;

public record FeedListDto(
	long feedId,
	String contents,
	LocalDateTime writeDate,
	long userId
) {
}

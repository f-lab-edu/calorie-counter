package com.example.flabcaloriecounter.feed.application.port.in.dto;

import java.time.LocalDateTime;

public record FeedListDto(
	long feedId,
	String contents,
	LocalDateTime writeDate,
	String userId,
	String userName,
	// photo가 없는경우 기본값이 null로 들어가야 mybatis에서 인식할수있기때문에 Long으로
	Long photoId,
	String photoName,
	String photoPath
) {
}

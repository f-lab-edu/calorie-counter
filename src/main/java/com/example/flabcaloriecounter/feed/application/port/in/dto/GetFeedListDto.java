package com.example.flabcaloriecounter.feed.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.feed.domain.Photo;

public record GetFeedListDto(
	long id,
	String contents,
	LocalDateTime writeDate,
	long userId,
	List<Photo> photos,
	int likeCount,
	LikeStatus likeStatus
	// todo 댓글추가
) {
}

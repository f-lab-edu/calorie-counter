package com.example.flabcaloriecounter.feed.domain;

public record Like(
	long id,
	long feedId,
	long userId,
	LikeStatus likeStatus
) {

}

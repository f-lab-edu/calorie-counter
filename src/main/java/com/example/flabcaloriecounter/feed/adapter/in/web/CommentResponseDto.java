package com.example.flabcaloriecounter.feed.adapter.in.web;

public record CommentResponseDto(
	long commentId,
	String contents,
	int groupNumber
) {
}

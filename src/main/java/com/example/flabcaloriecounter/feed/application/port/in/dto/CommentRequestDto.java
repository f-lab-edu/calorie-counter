package com.example.flabcaloriecounter.feed.application.port.in.dto;

public record CommentRequestDto(
	long commentId,
	String contents
) {
}

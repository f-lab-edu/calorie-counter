package com.example.flabcaloriecounter.feed.application.port.in.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
	private final long commentId;
	private final String contents;
	private final Long parentId;

	public CommentRequestDto() {
		this(1, "", null);
	}

	public CommentRequestDto(long commentId, String contents, Long parentId) {
		this.commentId = commentId;
		this.contents = contents;
		this.parentId = parentId;
	}

	public CommentRequestDto(String contents) {
		this(1, contents, null);
	}

	public CommentRequestDto(String contents, Long parentId) {
		this(1, contents, parentId);
	}
}

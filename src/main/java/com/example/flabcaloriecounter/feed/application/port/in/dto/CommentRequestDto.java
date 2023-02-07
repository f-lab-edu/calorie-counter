package com.example.flabcaloriecounter.feed.application.port.in.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
	private final long commentId;
	private final String contents;
	private final Long parentId;
	private final int groupNumber;

	public CommentRequestDto() {
		this(1, "", null, 0);
	}

	public CommentRequestDto(long commentId, String contents, Long parentId, int groupNumber) {
		this.commentId = commentId;
		this.contents = contents;
		this.parentId = parentId;
		this.groupNumber = groupNumber;
	}

	public CommentRequestDto(String contents) {
		this(1, contents, null, 0);
	}

	public CommentRequestDto(String contents, Long parentId) {
		this(1, contents, parentId, 0);
	}

	public CommentRequestDto(String contents, int groupNumber) {
		this(1, contents, null, groupNumber);
	}
}

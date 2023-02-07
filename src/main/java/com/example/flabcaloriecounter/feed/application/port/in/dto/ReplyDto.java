package com.example.flabcaloriecounter.feed.application.port.in.dto;

import lombok.Getter;

@Getter
public class ReplyDto {
	private final long commentId;
	private final long feedId;
	private final long userId;
	private final String contents;
	private final long parentId;
	private final int depth;
	private final int groupNumber;
	private final int refOrder;

	public ReplyDto(long commentId, long feedId, long userId, String contents, long parentId, int depth,
		int groupNumber,
		int refOrder) {
		this.commentId = commentId;
		this.feedId = feedId;
		this.userId = userId;
		this.contents = contents;
		this.parentId = parentId;
		this.depth = depth;
		this.groupNumber = groupNumber;
		this.refOrder = refOrder;
	}

	public ReplyDto(long feedId, long userId, String contents, long parentId, int depth, int groupNumber,
		int refOrder) {
		this(0, feedId, userId, contents, parentId, depth, groupNumber, refOrder);
	}
}

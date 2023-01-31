package com.example.flabcaloriecounter.feed.application.port.in.response;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.flabcaloriecounter.feed.domain.Comment;

import lombok.Getter;

@Getter
public class CommentDto {
	long id;
	long userId;
	LocalDateTime writeDate;
	String contents;
	Long parentId;
	int depth;
	List<CommentDto> list;

	public CommentDto(Comment firstComment) {
		this(firstComment.id(), firstComment.userId(), firstComment.writeDate(), firstComment.contents(),
			firstComment.parentId(), firstComment.depth(), null);
	}

	public CommentDto(long id, long userId, LocalDateTime writeDate, String contents, Long parentId, int depth,
		List<CommentDto> list) {
		this.id = id;
		this.userId = userId;
		this.writeDate = writeDate;
		this.contents = contents;
		this.parentId = parentId;
		this.depth = depth;
		this.list = list;
	}

	public void setList(List<CommentDto> list) {
		this.list = list;
	}

	public static List<CommentDto> createSequence(final List<Comment> allComment, final int depth) {
		final List<CommentDto> commentResults = new ArrayList<>();

		while (!allComment.isEmpty()) {
			final Comment firstComment = allComment.get(0);

			if (firstComment.depth() == depth) {
				commentResults.add(new CommentDto(firstComment));
				allComment.remove(0);
			} else if (firstComment.depth() > depth) {
				final List<CommentDto> childList = createSequence(allComment, depth + 1);

				commentResults.forEach(commentResult -> {
					final List<CommentDto> childResult = childList.stream()
						.filter(child -> commentResult.getId() == child.parentId)
						.collect(toList());

					if (!childResult.isEmpty()) {
						commentResult.setList(childResult);
					}
				});
			} else {
				return commentResults;
			}
		}

		return commentResults;
	}
}

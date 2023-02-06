package com.example.flabcaloriecounter.feed.application.port.in.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class FeedDto {
	private final long id;
	private final String contents;
	private final List<MultipartFile> photos;
	private final long userId;

	public FeedDto(long id, String contents, List<MultipartFile> photos, long userId) {
		this.id = id;
		this.contents = contents;
		this.photos = photos;
		this.userId = userId;
	}

	public FeedDto(final String contents, final List<MultipartFile> photos, long userId) {
		this(1, contents, photos, userId);
	}

	public FeedDto(final String contents, long userId) {
		this(1, contents, null, userId);
	}

	public FeedDto(List<MultipartFile> photos, long userId) {
		this(1, "", photos, userId);
	}
}



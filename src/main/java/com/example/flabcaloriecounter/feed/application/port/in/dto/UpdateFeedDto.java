package com.example.flabcaloriecounter.feed.application.port.in.dto;

import java.util.List;

public record UpdateFeedDto(
	String contents,
	List<ImageUploadDto> imageUploadDtos
) {
	public UpdateFeedDto(final String contents) {
		this(contents, null);
	}
}

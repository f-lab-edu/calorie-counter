package com.example.flabcaloriecounter.feed.application.port.in.dto;

public record ImageUploadDto(
	String imageName,
	String imagePath,
	long latestPostId
) {
}

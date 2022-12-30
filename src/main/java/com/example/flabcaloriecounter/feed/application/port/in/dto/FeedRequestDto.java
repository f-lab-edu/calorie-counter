package com.example.flabcaloriecounter.feed.application.port.in.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record FeedRequestDto(
	String contents,
	List<MultipartFile> photos
) {
}

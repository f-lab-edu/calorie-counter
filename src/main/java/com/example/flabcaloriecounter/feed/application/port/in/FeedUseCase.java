package com.example.flabcaloriecounter.feed.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.response.FeedDto;

public interface FeedUseCase {

	void write(final FeedDto feedDto, final List<MultipartFile> photos);
}

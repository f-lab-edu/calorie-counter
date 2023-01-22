package com.example.flabcaloriecounter.user.source;

import java.util.List;

import org.springframework.mock.web.MockMultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestFeedSource {

	private static final String WRITE_FEED_CONTENTS = "닭가슴살을 먹었다";
	private static final String WRITE_FEED_CONTENTS2 = "닭가슴살을 먹었다2";
	private static final String TEST_CONTENTS = "contents";
	private static final String EMPTY_CONTENT_TYPE = "";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static final MockMultipartFile mockImage1 = new MockMultipartFile(
		"photos",
		"photos",
		"image/jpeg",
		"photos".getBytes()
	);

	public static final MockMultipartFile mockImage2 = new MockMultipartFile(
		"photos",
		"photos2",
		"image/jpeg",
		"photos2".getBytes()
	);

	public static final MockMultipartFile contents = mapperExceptionCatch(WRITE_FEED_CONTENTS);
	public static final MockMultipartFile mockFeedContent = mapperExceptionCatch(WRITE_FEED_CONTENTS);

	private static MockMultipartFile mapperExceptionCatch(final String contents) {
		MockMultipartFile innerContents;
		try {
			innerContents = new MockMultipartFile(
				TEST_CONTENTS,
				TEST_CONTENTS,
				EMPTY_CONTENT_TYPE,
				objectMapper.writeValueAsString(new FeedTestDto(contents)).getBytes()
			);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return innerContents;
	}

	public static final FeedRequestDto contentsFeed = new FeedRequestDto(
		WRITE_FEED_CONTENTS,
		null
	);

	public static final FeedRequestDto contentsAndPhotoFeed = new FeedRequestDto(
		WRITE_FEED_CONTENTS2,
		List.of(mockImage1, mockImage2)
	);

	record FeedTestDto(String contents) {
	}
}

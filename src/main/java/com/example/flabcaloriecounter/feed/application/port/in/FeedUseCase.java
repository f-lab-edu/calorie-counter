package com.example.flabcaloriecounter.feed.application.port.in;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.example.flabcaloriecounter.feed.domain.Feed;

public interface FeedUseCase {

	void write(final FeedRequestDto feedRequestDto, final long userId);

	Optional<Feed> findByFeedId(final long feedId);

	void update(final String contents, final List<MultipartFile> photos, final String mockUserId, final long feedId);

	void delete(final String userId, final long feedId);
}

package com.example.flabcaloriecounter.feed.application.port.out;

import java.util.List;
import java.util.Optional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.response.FeedUpdateDto;
import com.example.flabcaloriecounter.feed.domain.Feed;

public interface FeedPort {

	long write(final String contents, final long userId);

	void update(final Feed feed, final FeedUpdateDto feedUpdateDto);

	Optional<Feed> findByFeedId(final long feedId);

	void insertImage(final List<ImageUploadDto> imagePathResult);
}

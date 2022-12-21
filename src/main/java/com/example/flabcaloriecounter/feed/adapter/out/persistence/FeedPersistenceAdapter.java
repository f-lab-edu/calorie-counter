package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.response.FeedUpdateDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedPersistenceAdapter implements FeedPort {

	private final FeedRepository feedRepository;

	@Override
	public long write(final String contents, final long userId) {
		return this.feedRepository.write(contents, userId);
	}

	@Override
	public void update(final Feed feed, final FeedUpdateDto feedUpdateDto) {
		this.feedRepository.update(feed, feedUpdateDto);
	}

	@Override
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedRepository.findByFeedId(feedId);
	}

	@Override
	public void insertImage(final List<ImageUploadDto> imagePathResult) {
		this.feedRepository.insertImage(imagePathResult);
	}

}

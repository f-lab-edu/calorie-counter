package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.feed.adapter.in.web.FeedInfoDto;
import com.example.flabcaloriecounter.feed.application.port.in.response.FeedUpdateDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.application.service.ImageUploadPath;
import com.example.flabcaloriecounter.feed.domain.Feed;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedPersistenceAdapter implements FeedPort {

	private final FeedRepository feedRepository;

	@Override
	public void write(final FeedInfoDto feedInfoDto) {
		this.feedRepository.write(feedInfoDto);
	}

	@Override
	public void update(final Feed feed, final FeedUpdateDto feedUpdateDto) {
		this.feedRepository.update(feed, feedUpdateDto);
	}

	@Override
	public Optional<Feed> findByFeedId(final Long feedId) {
		return this.feedRepository.findByFeedId(feedId);
	}

	@Override
	public void insertImage(final List<ImageUploadPath> uploadFile) {
		this.feedRepository.insertImage(uploadFile);
	}
}

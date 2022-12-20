package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.feed.adapter.in.web.FeedInfoDto;
import com.example.flabcaloriecounter.feed.application.port.in.response.FeedUpdateDto;
import com.example.flabcaloriecounter.feed.application.service.ImageUploadPath;
import com.example.flabcaloriecounter.feed.domain.Feed;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedMybatisRepository implements FeedRepository {

	private final FeedMapper feedMapper;

	@Override
	public void write(final FeedInfoDto feedInfoDto) {
		this.feedMapper.write(feedInfoDto);
	}

	@Override
	public void update(Feed feed, FeedUpdateDto feedUpdateDto) {
		this.feedMapper.update(feed, feedUpdateDto);
	}

	@Override
	public Optional<Feed> findByFeedId(final Long feedId) {
		return this.feedMapper.findByFeedId(feedId);
	}

	@Override
	public void insertImage(final List<ImageUploadPath> uploadFile) {
		this.feedMapper.insertImage(uploadFile);
	}
}

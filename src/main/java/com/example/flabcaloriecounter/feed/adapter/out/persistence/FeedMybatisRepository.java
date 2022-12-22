package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.domain.Feed;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedMybatisRepository implements FeedRepository {

	private final FeedMapper feedMapper;

	@Override
	public long write(final String contents, final long userId) {
		return this.feedMapper.write(contents, userId);
	}

	@Override
	public void update(final long feedId, final UpdateFeedDto feedDto) {
		if (feedDto.contents() == null) {
			return;
		}
		this.feedMapper.update(feedId, feedDto.contents());
	}

	@Override
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedMapper.findByFeedId(feedId);
	}

	@Override
	public void insertImage(final List<ImageUploadDto> imagePathResult) {
		this.feedMapper.insertImage(imagePathResult);
	}

	@Override
	public void updateImage(final long feedId, final List<UpdateImageInfo> updateImageInfos) {
		this.feedMapper.updateImage(feedId, updateImageInfos);
	}
}

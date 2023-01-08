package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.Photo;

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
		if (feedDto.contents() == null || "".equals(feedDto.contents())) {
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

	@Override
	public void delete(final long feedId) {
		this.feedMapper.delete(feedId);
	}

	@Override
	public List<FeedListDto> getFeedList(final Paging paging) {
		return this.feedMapper.getFeedList(paging);
	}

	@Override
	public long maxCursor() {
		return this.feedMapper.maxCursor();
	}

	@Override
	public List<Photo> photos(final long feedId) {
		return this.feedMapper.photos(feedId);
	}

	@Override
	public List<Image> findImageByFeedId(final long feedId) {
		return this.feedMapper.findImageByFeedId(feedId);
	}
}

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
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.Like;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.feed.domain.Photo;

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
	public void update(final long feedId, final UpdateFeedDto feedDto) {
		this.feedRepository.update(feedId, feedDto);
	}

	@Override
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedRepository.findByFeedId(feedId);
	}

	@Override
	public List<Image> findImageByFeedId(final long feedId) {
		return this.feedRepository.findImageByFeedId(feedId);
	}

	@Override
	public void insertImage(final List<ImageUploadDto> imagePathResult) {
		this.feedRepository.insertImage(imagePathResult);
	}

	@Override
	public void updateImage(final long feedId, final List<UpdateImageInfo> updateImageInfos) {
		this.feedRepository.updateImage(feedId, updateImageInfos);
	}

	@Override
	public void delete(final long feedId) {
		this.feedRepository.delete(feedId);
	}

	@Override
	public List<FeedListDto> getFeedList(final Paging paging) {
		return this.feedRepository.getFeedList(paging);
	}

	@Override
	public long maxCursor() {
		return this.feedRepository.maxCursor();
	}

	@Override
	public List<Photo> photos(final long feedId) {
		return this.feedRepository.photos(feedId);
	}

	@Override
	public Like findByFeedAndUser(final long userId, final long feedId) {
		return this.feedRepository.findByFeedAndUser(userId, feedId);
	}

	@Override
	public void like(final long userId, final long feedId, final LikeStatus likeStatus) {
		this.feedRepository.like(userId, feedId, likeStatus);
	}

	@Override
	public int likeCount(final long feedId, final LikeStatus likeStatus) {
		return this.feedRepository.likeCount(feedId, likeStatus);
	}

	@Override
	public void changeStatus(final long userId, final long feedId, final LikeStatus likeStatus) {
		this.feedRepository.unLike(userId, feedId, likeStatus);
	}

	@Override
	public LikeStatus findLikeStatusByUserId(final long feedId, final long mockUserId) {
		return this.feedRepository.findLikeStatusByUserId(feedId, mockUserId);
	}
}

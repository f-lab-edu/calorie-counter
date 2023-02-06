package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Comment;
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
	public void write(final FeedDto feedDto) {
		this.feedRepository.write(feedDto);
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
	public LikeStatus findLikeStatusByUserId(final long feedId, final long userId) {
		return this.feedRepository.findLikeStatusByUserId(feedId, userId);
	}

	@Override
	public long insertComment(final long feedId, final long userId, final CommentRequestDto commentRequestDto,
		final int group) {
		return this.feedRepository.insertComment(feedId, userId, commentRequestDto, group);
	}

	@Override
	public long insertReply(final long feedId, final long userId, final CommentRequestDto commentRequestDto,
		final int depth, final int groupNumber) {
		return this.feedRepository.insertReply(feedId, userId, commentRequestDto, depth, groupNumber);
	}

	@Override
	public Optional<Comment> findCommentById(final Long parentId) {
		return this.feedRepository.findCommentById(parentId);
	}

	@Override
	public List<Comment> comment(final long feedId, final int offset, final int commentPerPage) {
		return this.feedRepository.comment(feedId, offset, commentPerPage);
	}

	@Override
	public int countParent(final long feedId) {
		return this.feedRepository.countParent(feedId);
	}

}

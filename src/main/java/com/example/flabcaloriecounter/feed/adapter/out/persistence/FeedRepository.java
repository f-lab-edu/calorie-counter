package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ReplyDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.domain.Comment;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.Like;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.feed.domain.Photo;

public interface FeedRepository {
	void write(final FeedDto feedDto);

	void update(final long feedId, final UpdateFeedDto feedDto);

	Optional<Feed> findByFeedId(final long feedId);

	void insertImage(final List<ImageUploadDto> imagePathResult);

	void updateImage(final long feedId, final List<UpdateImageInfo> updateImageInfos);

	void delete(final long feedId);

	List<FeedListDto> getFeedList(final Paging paging);

	long maxCursor();

	List<Photo> photos(final long feedId);

	List<Image> findImageByFeedId(final long feedId);

	void like(final long userId, final long feedId, final LikeStatus likeStatus);

	int likeCount(final long feedId, final LikeStatus likeStatus);

	Like findByFeedAndUser(final long userId, final long feedId);

	void unLike(final long userId, final long feedId, final LikeStatus likeStatus);

	LikeStatus findLikeStatusByUserId(final long feedId, final long mockUserId);

	void insertComment(final long feedId, final long userId, final CommentRequestDto commentRequestDto);

	void insertReply(final ReplyDto replyDto);

	Optional<Comment> findCommentById(final Long parentId);

	List<Comment> comment(final long feedId, final int offset, final int commentPerPage);

	int countParent(final long feedId);

	int maxDepth(final int groupNumber);

	void updateRefOrder(final int parentOrderResult, final int groupNumber);

	void updateChild(final Long parentId);
}

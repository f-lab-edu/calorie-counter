package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.domain.Comment;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.Like;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.feed.domain.Photo;

@Mapper
public interface FeedMapper {

	void write(final FeedDto feedDto);

	void update(@Param("feedId") final long feedId, @Param("contents") final String contents);

	Optional<Feed> findByFeedId(final long feedId);

	void insertImage(@Param("imagePathResult") final List<ImageUploadDto> imagePathResult);

	void updateImage(@Param("feedId") final long feedId,
		@Param("updateImageInfo") final UpdateImageInfo updateImageInfo);

	void delete(final long feedId);

	List<FeedListDto> getFeedList(@Param("paging") final Paging paging);

	long maxCursor();

	List<Photo> photos(final long feedId);

	List<Image> findImageByFeedId(final long feedId);

	void like(@Param("userId") final long userId, @Param("feedId") final long feedId,
		@Param("likeStatus") final LikeStatus likeStatus);

	int likeCount(@Param("feedId") final long feedId, @Param("likeStatus") final LikeStatus likeStatus);

	Like findByFeedAndUser(@Param("userId") final long userId, @Param("feedId") final long feedId);

	void unLike(@Param("userId") final long userId, @Param("feedId") final long feedId,
		@Param("likeStatus") final LikeStatus likeStatus);

	LikeStatus findLikeStatusByUserId(@Param("feedId") final long feedId, @Param("userId") final long userId);

	long insertComment(@Param("feedId") final long feedId, @Param("userId") final long userId,
		@Param("commentRequestDto") final CommentRequestDto commentRequestDto, @Param("group") final int group);

	long insertReply(@Param("feedId") final long feedId, @Param("userId") final long userId,
		@Param("commentRequestDto") final CommentRequestDto commentRequestDto, @Param("depth") final int depth,
		@Param("groupNumber") final int groupNumber);

	Optional<Comment> findCommentById(@Param("parentId") final Long parentId);

	List<Comment> comment(@Param("feedId") final long feedId, @Param("offset") final int offset,
		@Param("commentPerPage") final int commentPerPage);

	int countParent(final long feedId);
}

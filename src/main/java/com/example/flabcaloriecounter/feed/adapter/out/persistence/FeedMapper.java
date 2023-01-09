package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.Like;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.feed.domain.Photo;

@Mapper
public interface FeedMapper {

	long write(@Param("contents") final String contents, @Param("userId") final long userId);

	void update(@Param("feedId") final long feedId, @Param("contents") final String contents);

	Optional<Feed> findByFeedId(final long feedId);

	void insertImage(@Param("imagePathResult") final List<ImageUploadDto> imagePathResult);

	void updateImage(@Param("feedId") final long feedId,
		@Param("updateImageInfos") final List<UpdateImageInfo> updateImageInfos);

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

	LikeStatus findLikeStatusByUserId(@Param("feedId") final long feedId, @Param("mockUserId") final long mockUserId);
}

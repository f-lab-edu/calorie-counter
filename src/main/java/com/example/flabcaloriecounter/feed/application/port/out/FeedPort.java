package com.example.flabcaloriecounter.feed.application.port.out;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.Like;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.feed.domain.Photo;

public interface FeedPort {

	long write(final String contents, final long userId);

	void update(final long feedID, final UpdateFeedDto feedDto);

	Optional<Feed> findByFeedId(final long feedId);

	List<Image> findImageByFeedId(final long feedId);

	void insertImage(final List<ImageUploadDto> imagePathResult);

	void updateImage(final long feedId, final List<UpdateImageInfo> updateImageInfos);

	void delete(final long feedId);

	List<FeedListDto> getFeedList(final Paging paging);

	long maxCursor();

	List<Photo> photos(final long feedId);

	Like findByFeedAndUser(final long userId, final long feedId);

	void like(final long userId, final long feedId, final LikeStatus likeStatus);

	int likeCount(final long feedId, final LikeStatus likeStatus);

	void changeStatus(final long userId, final long feedId, final LikeStatus likeStatus);

	LikeStatus findLikeStatusByUserId(final long feedId, final long mockUserId);
}

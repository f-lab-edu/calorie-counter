package com.example.flabcaloriecounter.feed.application.port.out;

import java.util.List;
import java.util.Optional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.domain.Feed;

public interface FeedPort {

	long write(final String contents, final long userId);

	void update(final long feedID, final UpdateFeedDto feedDto);

	Optional<Feed> findByFeedId(final long feedId);

	void insertImage(final List<ImageUploadDto> imagePathResult);

	void updateImage(final long feedId, final List<UpdateImageInfo> updateImageInfos);
}

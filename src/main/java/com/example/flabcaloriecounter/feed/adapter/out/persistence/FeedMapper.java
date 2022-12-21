package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.response.FeedUpdateDto;
import com.example.flabcaloriecounter.feed.domain.Feed;

@Mapper
public interface FeedMapper {

	long write(@Param("contents") final String contents, @Param("userId") final long userId);

	void update(final Feed feed, final FeedUpdateDto feedUpdateDto);

	Optional<Feed> findByFeedId(final long feedId);

	void insertImage(@Param("imagePathResult") final List<ImageUploadDto> imagePathResult);
}

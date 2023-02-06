package com.example.flabcaloriecounter.feed.application.port.in;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.domain.Feed;

public interface FeedUseCase {
	void write(final FeedDto feedDto);

	Optional<Feed> findByFeedId(final long feedId);

	void update(final String contents, final List<MultipartFile> photos, final long userId, final long feedId);

	void delete(final long userId, final long feedId);

	List<FeedListDto> getFeedList(final Paging paging);

	long maxCursor();

	List<GetFeedListDto> feedListWithPhoto(final List<FeedListDto> feedList, final long userId,
		final int commentPageNum, final int commentPerPage);

	void like(final long feedId, final long userId);

	long comment(final long feedId, final long userId, final CommentRequestDto commentRequestDto);

	long reply(final long feedId, final long userId, final CommentRequestDto commentRequestDto);
}

package com.example.flabcaloriecounter.feed.application.port.in;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.domain.Feed;

public interface FeedUseCase {

	void write(final FeedRequestDto feedRequestDto, final String userId);

	Optional<Feed> findByFeedId(final long feedId);

	void update(final String contents, final List<MultipartFile> photos, final String mockUserId, final long feedId);

	void delete(final String userId, final long feedId);

	List<FeedListDto> getFeedList(final Paging paging);

	long maxCursor();

	List<GetFeedListDto> feedListWithPhoto(final List<FeedListDto> feedList, final long mockUserId);

	void like(final long feedId, final String userId);
}

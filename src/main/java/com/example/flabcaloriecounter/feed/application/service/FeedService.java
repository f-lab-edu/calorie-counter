package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService implements FeedUseCase {

	private final FeedPort feedPort;
	private final ImageService imageService;

	@Override
	@Transactional
	public void write(final FeedDto feedDto, final long userId) {
		//todo 현재 유저가 존재하는 아이디인지 체크

		//todo write(), uploadFile()에 인증된 userId(현재는 임시Id) 넣어줘야한다.
		if (contentsAndPhotos(feedDto)) {
			this.feedPort.insertImage(imageInfos(feedDto, userId, this.feedPort.write(feedDto.contents(), userId)));
			return;
		}

		if (onlyContents(feedDto)) {
			this.feedPort.write(feedDto.contents(), userId);
			return;
		}

		if (onlyPhotos(feedDto)) {
			this.feedPort.insertImage(imageInfos(feedDto, userId, this.feedPort.write("", userId)));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedPort.findByFeedId(feedId);
	}

	private List<ImageUploadDto> imageInfos(final FeedDto feedDto, final long userId, final long latestPostId) {
		return imageService.uploadFile(feedDto.photos(), userId).stream()
			.map(imageUploadPath -> new ImageUploadDto(imageUploadPath.imageName(), imageUploadPath.imagePath(),
				latestPostId))
			.toList();
	}

	private boolean onlyPhotos(final FeedDto feedDto) {
		return feedDto.contents() == null;
	}

	private boolean onlyContents(final FeedDto feedDto) {
		return feedDto.photos() == null;
	}

	private boolean contentsAndPhotos(final FeedDto feedDto) {
		return feedDto.photos() != null;
	}
}

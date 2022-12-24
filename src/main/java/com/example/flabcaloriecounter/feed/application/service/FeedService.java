package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.FeedNotFoundException;
import com.example.flabcaloriecounter.exception.InvalidUserException;
import com.example.flabcaloriecounter.exception.UserNotFoundException;
import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService implements FeedUseCase {

	private final FeedPort feedPort;
	private final ImageService imageService;
	private final SignUpPort signUpPort;

	@Override
	@Transactional
	public void write(final FeedDto feedDto, final long userId) {
		//todo 현재 유저가 존재하는 아이디인지 체크

		//todo write(), uploadFile()에 인증된 userId(현재는 임시Id) 넣어줘야한다.
		if (onlyContents(feedDto)) {
			this.feedPort.write(feedDto.contents(), userId);
		} else if (onlyPhotos(feedDto)) {
			this.feedPort.insertImage(imageInfos(feedDto, userId, this.feedPort.write("", userId)));
		} else {
			this.feedPort.insertImage(imageInfos(feedDto, userId, this.feedPort.write(feedDto.contents(), userId)));
		}
	}

	@Override
	@Transactional
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedPort.findByFeedId(feedId);
	}

	@Override
	@Transactional
	public void update(final FeedDto feedDto, final String mockUserId, final long feedId) {
		final User user = this.signUpPort.findByUserId(mockUserId)
			.orElseThrow(() -> new UserNotFoundException(String.format("%s not exist", mockUserId)));

		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new FeedNotFoundException(String.format("%s not exist", feedId)));

		if (user.id() != feed.userId()) {
			throw new InvalidUserException(String.format("%s is not match feedWriter", user.id()));
		}

		if (feedDto.photos() != null) {
			final List<UpdateImageInfo> updateImageInfos = imageService.uploadFile(feedDto.photos(), user.id()).stream()
				.map(imageUploadPath -> new UpdateImageInfo(imageUploadPath.imageName(), imageUploadPath.imagePath()))
				.toList();

			this.feedPort.updateImage(feedId, updateImageInfos);
		}

		this.feedPort.update(feedId, new UpdateFeedDto(feedDto.contents()));
	}

	private List<ImageUploadDto> imageInfos(final FeedDto feedDto, final long userId, final long latestPostId) {
		return imageService.uploadFile(feedDto.photos(), userId).stream()
			.map(imageUploadPath -> new ImageUploadDto(imageUploadPath.imageName(), imageUploadPath.imagePath(),
				latestPostId))
			.toList();
	}

	private boolean onlyPhotos(final FeedDto feedDto) {
		return feedDto.contents().isEmpty();
	}

	private boolean onlyContents(final FeedDto feedDto) {
		return feedDto.photos() == null;
	}
}

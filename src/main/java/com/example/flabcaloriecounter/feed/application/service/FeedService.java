package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.exception.FeedNotFoundException;
import com.example.flabcaloriecounter.exception.InvalidUserException;
import com.example.flabcaloriecounter.exception.UserNotFoundException;
import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
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
	public void write(final FeedRequestDto feedRequestDto, final long userId) {
		//todo 현재 유저가 존재하는 아이디인지 체크

		//todo write(), uploadFile()에 인증된 userId(현재는 임시Id) 넣어줘야한다.

		// 컨텐츠만 존재하는경우
		if (onlyContents(feedRequestDto)) {
			this.feedPort.write(feedRequestDto.contents(), userId);
		}
		// 사진만 존재하는경우
		else if (onlyPhotos(feedRequestDto)) {
			this.feedPort.insertImage(imageInfos(feedRequestDto.photos(), userId, this.feedPort.write("", userId)));
		}
		// 둘다 존재하는경우
		else {
			this.feedPort.insertImage(
				imageInfos(feedRequestDto.photos(), userId, this.feedPort.write(feedRequestDto.contents(), userId)));
		}
	}

	@Override
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedPort.findByFeedId(feedId);
	}

	@Override
	@Transactional
	public void update(final String contents, final List<MultipartFile> photos, final String mockUserId,
		final long feedId) {
		final User user = this.signUpPort.findByUserId(mockUserId)
			.orElseThrow(() -> new UserNotFoundException(String.format("%s not exist", mockUserId)));

		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new FeedNotFoundException(String.format("%s not exist", feedId)));

		if (user.id() != feed.userId()) {
			throw new InvalidUserException(String.format("%s is not match feedWriter", user.id()));
		}

		if (photos != null && photos.stream().noneMatch(MultipartFile::isEmpty)) {
			final List<UpdateImageInfo> updateImageInfos = imageService.uploadFile(photos, user.id()).stream()
				.map(imageUploadPath -> new UpdateImageInfo(imageUploadPath.imageName(), imageUploadPath.imagePath()))
				.toList();

			this.feedPort.updateImage(feedId, updateImageInfos);
		}

		this.feedPort.update(feedId, new UpdateFeedDto(contents));
	}

	@Override
	@Transactional
	public void delete(final String userId, final long feedId) {
		final User user = this.signUpPort.findByUserId(userId)
			.orElseThrow(() -> new UserNotFoundException(String.format("%s not exist", userId)));

		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new FeedNotFoundException(String.format("%s not exist", feedId)));

		if (user.id() != feed.userId()) {
			throw new InvalidUserException(String.format("%s is not match feedWriter", user.id()));
		}

		this.feedPort.delete(feedId);
	}

	private List<ImageUploadDto> imageInfos(final List<MultipartFile> photos, final long userId,
		final long latestPostId) {
		return imageService.uploadFile(photos, userId).stream()
			.map(imageUploadPath -> new ImageUploadDto(imageUploadPath.imageName(), imageUploadPath.imagePath(),
				latestPostId))
			.toList();
	}

	private boolean onlyPhotos(final FeedRequestDto feedRequestDto) {
		return "".equals(feedRequestDto.contents());
	}

	private boolean onlyContents(final FeedRequestDto feedRequestDto) {
		return feedRequestDto.photos() == null || feedRequestDto.photos().stream().anyMatch(MultipartFile::isEmpty);
	}
}

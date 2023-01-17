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
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
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
	public void write(final FeedRequestDto feedRequestDto, final String userId) {
		final User user = this.signUpPort.findByUserId(userId)
			.orElseThrow(() -> new UserNotFoundException(String.format("userId %s not exist", userId)));

		final long feedId = this.feedPort.write(feedRequestDto.contents(), user.id());

		if (feedRequestDto.photos() != null && feedRequestDto.photos().stream().noneMatch(MultipartFile::isEmpty)) {
			this.feedPort.insertImage(imageInfos(feedRequestDto.photos(), user.id(), feedId));
		}
	}

	@Override
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedPort.findByFeedId(feedId);
	}

	@Override
	@Transactional
	public void update(final String contents, final List<MultipartFile> photos, final String userId,
		final long feedId) {
		final User user = this.signUpPort.findByUserId(userId)
			.orElseThrow(() -> new UserNotFoundException(String.format("%s not exist", userId)));

		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new FeedNotFoundException(String.format("%s not exist", feedId), "존재하지 않는 피드입니다."));

		if (user.id() != feed.userId()) {
			throw new InvalidUserException(String.format("%s is not match feedWriter", user.id()), "권한이 없는 유저입니다");
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
			.orElseThrow(() -> new FeedNotFoundException(String.format("%s not exist", feedId), "존재하지 않는 피드입니다."));

		if (user.id() != feed.userId()) {
			throw new InvalidUserException(String.format("%s is not match feedWriter", user.id()), "권한이 없는 유저입니다");
		}

		this.feedPort.delete(feedId);
	}

	@Override
	public List<FeedListDto> getFeedList(final Paging paging) {
		return this.feedPort.getFeedList(paging);
	}

	@Override
	public long maxCursor() {
		return this.feedPort.maxCursor();
	}

	@Override
	public List<GetFeedListDto> feedListWithPhoto(final List<FeedListDto> feedList, final long mockUserId) {
		return feedList.stream()
			.map(
				feedListDto -> new GetFeedListDto(
					feedListDto.feedId(),
					feedListDto.contents(),
					feedListDto.writeDate(),
					feedListDto.userId(),
					this.feedPort.photos(feedListDto.feedId()),
					likeCount(feedListDto.feedId()),
					this.feedPort.findLikeStatusByUserId(feedListDto.feedId(), mockUserId)))
			.toList();
	}

	@Override
	@Transactional
	public void like(final long feedId, final String userId) {
		final User user = this.signUpPort.findByUserId(userId)
			.orElseThrow(() -> new UserNotFoundException(String.format("%s not exist", userId)));

		this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new FeedNotFoundException(String.format("%s not exist", feedId), "존재하지 않는 피드입니다."));

		// 해당 피드에 좋아요를 누른적있는지 확인
		if (this.feedPort.findByFeedAndUser(user.id(), feedId) != null
			&& this.feedPort.findByFeedAndUser(user.id(), feedId).likeStatus() == LikeStatus.ACTIVATE) {
			this.feedPort.changeStatus(user.id(), feedId, LikeStatus.NOT_ACTIVATE);
		}
		// 좋아요를 누른적있고, 비활성화 상태인경우
		else if (this.feedPort.findByFeedAndUser(user.id(), feedId) != null) {
			this.feedPort.changeStatus(user.id(), feedId, LikeStatus.ACTIVATE);
		}
		// 처음 좋아요를 누른경우
		else {
			this.feedPort.like(user.id(), feedId, LikeStatus.ACTIVATE);
		}
	}

	public int likeCount(final long feedId) {
		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new FeedNotFoundException(String.format("%s not exist", feedId), "존재하지 않는 피드입니다."));

		return this.feedPort.likeCount(feed.id(), LikeStatus.ACTIVATE);
	}

	private List<ImageUploadDto> imageInfos(final List<MultipartFile> photos, final long userId,
		final long latestPostId) {
		return imageService.uploadFile(photos, userId).stream()
			.map(imageUploadPath -> new ImageUploadDto(imageUploadPath.imageName(), imageUploadPath.imagePath(),
				latestPostId))
			.toList();
	}
}

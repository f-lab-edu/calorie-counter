package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateFeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.UpdateImageInfo;
import com.example.flabcaloriecounter.feed.application.port.in.response.CommentDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Comment;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.Like;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.util.StatusEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService implements FeedUseCase {

	private final FeedPort feedPort;
	private final ImageService imageService;

	@Override
	@Transactional
	public void write(final FeedDto feedDto) {
		this.feedPort.write(feedDto);

		if (feedDto.getPhotos() != null && feedDto.getPhotos()
			.stream()
			.noneMatch(MultipartFile::isEmpty)) {
			this.feedPort.insertImage(imageInfos(feedDto.getPhotos(), feedDto.getUserId(), feedDto.getId()));
		}
	}

	@Override
	public Optional<Feed> findByFeedId(final long feedId) {
		return this.feedPort.findByFeedId(feedId);
	}

	@Override
	@Transactional
	public void update(final String contents, final List<MultipartFile> photos, final long userId,
		final long feedId) {
		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(StatusEnum.FEED_NOT_FOUND, String.format("%s not exist", feedId)));

		if (userId != feed.userId()) {
			throw new CustomException(StatusEnum.INVALID_USER, String.format("%s is not match feedWriter", userId));
		}

		if (photos != null && photos.stream().noneMatch(MultipartFile::isEmpty)) {
			final List<UpdateImageInfo> updateImageInfos = imageService.uploadFile(photos, userId).stream()
				.map(imageUploadPath -> new UpdateImageInfo(imageUploadPath.imageName(), imageUploadPath.imagePath()))
				.toList();

			this.feedPort.updateImage(feedId, updateImageInfos);
		}

		this.feedPort.update(feedId, new UpdateFeedDto(contents));
	}

	@Override
	@Transactional
	public void delete(final long userId, final long feedId) {
		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(StatusEnum.FEED_NOT_FOUND, String.format("%s not exist", feedId)));

		if (userId != feed.userId()) {
			throw new CustomException(StatusEnum.INVALID_USER, String.format("%s is not match feedWriter", userId));
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
	public List<GetFeedListDto> feedListWithPhoto(final List<FeedListDto> feedList, final long userId,
		final int commentPageNum, final int commentPerPage) {
		return feedList.stream()
			.map(
				feedListDto -> new GetFeedListDto(
					feedListDto.feedId(),
					feedListDto.contents(),
					feedListDto.writeDate(),
					feedListDto.userId(),
					this.feedPort.photos(feedListDto.feedId()),
					likeCount(feedListDto.feedId()),
					this.feedPort.findLikeStatusByUserId(feedListDto.feedId(), userId),
					CommentDto.createSequence(
						this.feedPort.comment(feedListDto.feedId(), (commentPageNum - 1) * commentPerPage,
							commentPerPage), 0)
				))
			.toList();
	}

	@Override
	@Transactional
	public void like(final long feedId, final long userId) {
		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(StatusEnum.FEED_NOT_FOUND, String.format("%s not exist", feedId)));

		final Like like = this.feedPort.findByFeedAndUser(userId, feedId);

		if (like == null) {
			this.feedPort.like(userId, feedId, LikeStatus.ACTIVATE);
		} else if (like.likeStatus() == LikeStatus.ACTIVATE) {
			this.feedPort.changeStatus(userId, feedId, LikeStatus.NOT_ACTIVATE);
		} else {
			this.feedPort.changeStatus(userId, feedId, LikeStatus.ACTIVATE);
		}
	}

	@Override
	@Transactional
	public long comment(final long feedId, final long userId, final CommentRequestDto commentRequestDto) {
		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(StatusEnum.FEED_NOT_FOUND, String.format("%s not exist", feedId)));

		// 부모댓글의 group번호는 해당피드에 존재하는 부모댓글의 개수 +1로 설정
		return this.feedPort.insertComment(feedId, userId, commentRequestDto, this.feedPort.countParent(feedId) + 1);
	}

	@Override
	@Transactional
	public long reply(final long feedId, final long userId, final CommentRequestDto commentRequestDto) {
		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(StatusEnum.FEED_NOT_FOUND, String.format("%s not exist", feedId)));

		final Comment comment = this.feedPort.findCommentById(commentRequestDto.getParentId())
			.orElseThrow(
				() -> new CustomException(StatusEnum.COMMENT_NOT_FOUND,
					String.format("%s not exist", commentRequestDto.getParentId())));

		// 부모가있는 댓글들은 부모댓글의 depth+1의 깊이로 설정, 부모댓글의 groupNum으로 묶인다.
		return this.feedPort.insertReply(feedId, userId, commentRequestDto, comment.depth() + 1, comment.groupNumber());
	}

	public int likeCount(final long feedId) {
		final Feed feed = this.feedPort.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(StatusEnum.FEED_NOT_FOUND, String.format("%s not exist", feedId)));

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

package com.example.flabcaloriecounter.feed.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.config.UserAuthentication;
import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.util.CustomResponse;
import com.example.flabcaloriecounter.util.StatusEnum;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final FeedUseCase feedUseCase;

	@PostMapping
	public ResponseEntity<CustomResponse<Void>> write(final UserAuthentication userAuthentication,
		@RequestPart(required = false) final List<MultipartFile> photos,
		@RequestPart(required = false) final String contents) {
		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new CustomException(StatusEnum.EMPTY_FEED);
		}

		this.feedUseCase.write(new FeedDto(contents, photos, userAuthentication.id()));
		return CustomResponse.created();
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<CustomResponse<Void>> update(final UserAuthentication userAuthentication,
		@RequestPart(required = false) final List<MultipartFile> photos,
		@RequestPart(required = false) final String contents,
		@PathVariable final long feedId) {
		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new CustomException(StatusEnum.EMPTY_FEED);
		}

		this.feedUseCase.update(contents, photos, userAuthentication.id(), feedId);
		return CustomResponse.created();
	}

	@GetMapping
	public ResponseEntity<CustomResponse<List<GetFeedListDto>>> feedList(final UserAuthentication authentication,
		@RequestParam final long cursorNo,
		@RequestParam(required = false, defaultValue = "5") final int displayPerPage,
		@RequestParam(required = false, defaultValue = "1") final int commentPageNum,
		@RequestParam(required = false, defaultValue = "30") final int commentPerPage) {
		if (cursorNo <= 0) {
			final List<FeedListDto> feedList = this.feedUseCase.getFeedList(
				new Paging(this.feedUseCase.maxCursor(), displayPerPage));
			return CustomResponse.ok(this.feedUseCase.feedListWithPhoto(feedList, authentication.id(),
				commentPageNum, commentPerPage));
		}

		final List<FeedListDto> feedList = this.feedUseCase.getFeedList(
			new Paging(cursorNo, displayPerPage));
		return CustomResponse.ok(this.feedUseCase.feedListWithPhoto(feedList, authentication.id(),
			commentPageNum, commentPerPage));
	}

	@PostMapping("/{feedId}/like")
	public ResponseEntity<CustomResponse<Void>> like(final UserAuthentication authentication,
		@PathVariable final long feedId) {
		this.feedUseCase.like(feedId, authentication.id());
		return CustomResponse.created();
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<CustomResponse<Void>> delete(final UserAuthentication authentication,
		@PathVariable final long feedId) {
		this.feedUseCase.delete(authentication.id(), feedId);
		return CustomResponse.ok();
	}

	@PostMapping("/{feedId}/comment")
	public ResponseEntity<CustomResponse<CommentResponseDto>> comment(final UserAuthentication userAuthentication,
		@PathVariable final long feedId, @RequestBody final CommentRequestDto commentRequestDto) {
		if (commentRequestDto.getParentId() == null) {
			this.feedUseCase.comment(feedId, userAuthentication.id(), commentRequestDto);
			return CustomResponse.created(
				new CommentResponseDto(commentRequestDto.getCommentId(), commentRequestDto.getContents()));
		}
		this.feedUseCase.reply(feedId, userAuthentication.id(), commentRequestDto);
		return CustomResponse.created(
			new CommentResponseDto(commentRequestDto.getCommentId(), commentRequestDto.getContents()));
	}

	//todo 수정, 삭제
}

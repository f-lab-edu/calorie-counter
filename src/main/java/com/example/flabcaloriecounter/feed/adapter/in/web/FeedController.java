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
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
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
		@RequestPart(value = "photos", required = false) final List<MultipartFile> photos,
		@RequestPart(value = "contents", required = false) final String contents) {
		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new CustomException(StatusEnum.EMPTY_FEED);
		}

		this.feedUseCase.write(new FeedRequestDto(contents, photos), userAuthentication.userId());
		return CustomResponse.created();
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<CustomResponse<Void>> update(final UserAuthentication userAuthentication,
		@RequestPart(value = "photos", required = false) final List<MultipartFile> photos,
		@RequestPart(value = "contents", required = false) final String contents,
		@PathVariable final long feedId) {
		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new CustomException(StatusEnum.EMPTY_FEED);
		}

		this.feedUseCase.update(contents, photos, userAuthentication.userId(), feedId);
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
		this.feedUseCase.like(feedId, authentication.userId());
		return CustomResponse.created();
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<CustomResponse<Void>> delete(final UserAuthentication authentication,
		@PathVariable final long feedId) {
		this.feedUseCase.delete(authentication.userId(), feedId);
		return CustomResponse.ok();
	}

	@PostMapping("/{feedId}/comment")
	public ResponseEntity<CustomResponse<CommentResponseDto>> comment(final UserAuthentication userAuthentication,
		@RequestParam(required = false) final Long parentId,
		@PathVariable final long feedId,
		@RequestBody final CommentRequestDto commentDto) {
		if (parentId == null) {
			final long commentId = this.feedUseCase.comment(feedId, userAuthentication.id(), commentDto.contents());
			return CustomResponse.created(new CommentResponseDto(commentId, commentDto.contents()));
		}
		final long commentId = this.feedUseCase.reply(feedId, userAuthentication.id(), commentDto.contents(), parentId);
		return CustomResponse.created(new CommentResponseDto(commentId, commentDto.contents()));
	}

	//todo 수정, 삭제
}

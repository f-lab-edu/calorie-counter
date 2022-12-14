package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.EMPTY_FEED_MSG;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
@Validated
public class FeedController {

	private final FeedUseCase feedUseCase;

	@PostMapping
	public ResponseEntity<Void> write(
		@RequestPart(value = "photos", required = false) final List<MultipartFile> photos,
		@RequestPart(value = "contents", required = false) final String contents) {
		//todo 로그인 되어있지않은경우 예외처리

		//todo 로그인유저 정보 넘겨줘야한다.

		// 둘 다 비어있는경우
		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new IllegalArgumentException(EMPTY_FEED_MSG);
		}

		final long mockUserId = 1;
		this.feedUseCase.write(new FeedRequestDto(contents, photos), mockUserId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<Void> update(
		@RequestPart(value = "photos", required = false) final List<MultipartFile> photos,
		@RequestPart(value = "contents", required = false) final String contents,
		@PathVariable final long feedId) {
		//todo 로그인 되어있지않은경우 예외처리

		//todo update()에 로그인유저 정보 넘겨줘야한다.

		// 둘 다 비어있는경우
		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new IllegalArgumentException(EMPTY_FEED_MSG);
		}

		final String mockUserId = "mockUser";
		this.feedUseCase.update(contents, photos, mockUserId, feedId);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<GetFeedListDto>> feedList(@RequestParam final long cursorNo,
		@RequestParam(required = false, defaultValue = "5") final int displayPerPage) {

		// 로그인 한 유저(임시)
		final long mockUserId = 1;

		if (cursorNo <= 0) {
			final List<FeedListDto> feedList = this.feedUseCase.getFeedList(
				new Paging(this.feedUseCase.maxCursor(), displayPerPage));

			return new ResponseEntity<>(this.feedUseCase.feedListWithPhoto(feedList, mockUserId), HttpStatus.OK);
		}

		final List<FeedListDto> feedList = this.feedUseCase.getFeedList(
			new Paging(cursorNo, displayPerPage));

		return new ResponseEntity<>(this.feedUseCase.feedListWithPhoto(feedList, mockUserId), HttpStatus.OK);
	}

	@PostMapping("/{feedId}/like")
	public ResponseEntity<Void> like(@PathVariable final long feedId) {
		final String mockUserId = "mockUser";

		this.feedUseCase.like(feedId, mockUserId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<Void> delete(@PathVariable final long feedId) {
		//todo 로그인 되어있지않은경우 예외처리

		//todo update()에 로그인유저 정보 넘겨줘야한다.

		final String mockUserId = "mockUser";
		this.feedUseCase.delete(mockUserId, feedId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

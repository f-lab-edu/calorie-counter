package com.example.flabcaloriecounter.feed.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.example.flabcaloriecounter.config.UserAuthentication;
import com.example.flabcaloriecounter.exception.EmptyFeedException;
import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final FeedUseCase feedUseCase;

	@PostMapping
	public ResponseEntity<Void> write(final UserAuthentication userAuthentication,
		@RequestPart(value = "photos", required = false) final List<MultipartFile> photos,
		@RequestPart(value = "contents", required = false) final String contents) {

		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new EmptyFeedException("피드 내용이 비어있습니다");
		}

		this.feedUseCase.write(new FeedRequestDto(contents, photos), userAuthentication.userId());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<Void> update(final UserAuthentication userAuthentication,
		@RequestPart(value = "photos", required = false) final List<MultipartFile> photos,
		@RequestPart(value = "contents", required = false) final String contents,
		@PathVariable final long feedId) {

		if ((contents == null || "".equals(contents)) && (photos == null || photos.stream()
			.anyMatch(MultipartFile::isEmpty))) {
			throw new EmptyFeedException("피드 내용이 비어있습니다");
		}

		this.feedUseCase.update(contents, photos, userAuthentication.userId(), feedId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<GetFeedListDto>> feedList(final UserAuthentication authentication,
		@RequestParam final long cursorNo,
		@RequestParam(required = false, defaultValue = "5") final int displayPerPage) {

		if (cursorNo <= 0) {
			final List<FeedListDto> feedList = this.feedUseCase.getFeedList(
				new Paging(this.feedUseCase.maxCursor(), displayPerPage));

			return new ResponseEntity<>(this.feedUseCase.feedListWithPhoto(feedList, authentication.id()),
				HttpStatus.OK);
		}

		final List<FeedListDto> feedList = this.feedUseCase.getFeedList(
			new Paging(cursorNo, displayPerPage));

		return new ResponseEntity<>(this.feedUseCase.feedListWithPhoto(feedList, authentication.id()), HttpStatus.OK);
	}

	@PostMapping("/{feedId}/like")
	public ResponseEntity<Void> like(final UserAuthentication authentication, @PathVariable final long feedId) {
		this.feedUseCase.like(feedId, authentication.userId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{feedId}")
	public ResponseEntity<Void> delete(final UserAuthentication authentication, @PathVariable final long feedId) {
		this.feedUseCase.delete(authentication.userId(), feedId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

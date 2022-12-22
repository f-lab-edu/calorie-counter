package com.example.flabcaloriecounter.feed.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final FeedUseCase feedUseCase;

	@PostMapping
	public ResponseEntity<FeedDto> write(@RequestPart final FeedDto feedDto) {
		//todo 로그인 되어있지않은경우 예외처리

		//todo write()에 로그인유저 정보 넘겨줘야한다.
		final long mockUserId = 1;
		this.feedUseCase.write(feedDto, mockUserId);
		return new ResponseEntity<>(feedDto, HttpStatus.CREATED);
	}

	@PutMapping("/{feedId}")
	public ResponseEntity<FeedDto> update(@RequestPart final FeedDto feedDto, @PathVariable final long feedId) {
		//todo 로그인 되어있지않은경우 예외처리

		//todo update()에 로그인유저 정보 넘겨줘야한다.

		final String mockUserId = "mockUser";
		this.feedUseCase.update(feedDto, mockUserId, feedId);
		return new ResponseEntity<>(feedDto, HttpStatus.CREATED);
	}
}

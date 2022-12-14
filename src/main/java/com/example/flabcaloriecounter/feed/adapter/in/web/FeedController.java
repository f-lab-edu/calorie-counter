package com.example.flabcaloriecounter.feed.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.response.FeedDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

	private final FeedUseCase feedUseCase;

	@PostMapping
	public ResponseEntity<FeedDto> write(@RequestParam("content") final FeedDto feedDto,
		@RequestPart final List<MultipartFile> photos) {
		//todo 로그인 되어있지않은경우 예외처리

		//todo write()에 로그인유저 정보 넘겨줘야한다.

		this.feedUseCase.write(feedDto, photos);
		return new ResponseEntity<>(feedDto, HttpStatus.CREATED);
	}
}

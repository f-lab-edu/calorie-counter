package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.adapter.in.web.FeedInfoDto;
import com.example.flabcaloriecounter.feed.application.port.in.FeedUseCase;
import com.example.flabcaloriecounter.feed.application.port.in.response.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService implements FeedUseCase {

	private final FeedPort feedPort;
	private final ImageService imageService;

	@Override
	@Transactional
	public void write(final FeedDto feedDto, final List<MultipartFile> photos) {
		//todo 현재 유저가 존재하는 아이디인지 체크

		//todo write()에 인증된 userId 넣어줘야한다.
		Long mockUserId = 1L;
		this.feedPort.write(new FeedInfoDto(feedDto.contents(), mockUserId));

		//todo uploadFile()에 인증된 userId 넣어줘야한다.
		this.feedPort.insertImage(this.imageService.uploadFile(photos));
	}
}

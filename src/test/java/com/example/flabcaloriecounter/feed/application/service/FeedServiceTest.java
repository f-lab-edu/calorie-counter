package com.example.flabcaloriecounter.feed.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserRepository;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserType;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FeedServiceTest {

	@Autowired
	private FeedService feedService;

	@Autowired
	private FeedPort feedPort;

	@Autowired
	private UserRepository userRepository;

	private FeedDto contentsFeed;
	private SignUpForm signUpForm;
	private List<ImageUploadDto> imageInfos;
	private List<ImageUploadDto> onlyImageInfos;

	@BeforeEach
	void setup() {
		this.contentsFeed = new FeedDto(
			"닭가슴살을 먹었다",
			null
		);

		this.signUpForm = new SignUpForm(
			"user",
			"이영진",
			"12345678",
			"dudwls0505@naver.com",
			60.02,
			UserType.ORDINARY,
			JudgeStatus.getInitialJudgeStatusByUserType(UserType.ORDINARY)
		);
	}

	//todo 로그인 유무 check
	@Test
	@DisplayName("피드 작성 성공 : contents만 있음 ")
	void feed_write_success() {
		this.userRepository.signUp(this.signUpForm);

		assertDoesNotThrow(() -> this.feedService.write(this.contentsFeed, 1));

		assertThat(this.feedService.findByFeedId(1)).isEqualTo(
			Optional.of(new Feed(1, this.contentsFeed.contents(), 1)));
	}

	//todo 로그인 유무 check
	@Test
	@DisplayName("피드 작성 성공 : contents, Image 둘다 있음")
	void feed_write_success2() {
		this.userRepository.signUp(this.signUpForm);

		this.imageInfos = List.of(
			new ImageUploadDto("image1.png", "local/2022/1221/user12q3wqeqwe.png", 1),
			new ImageUploadDto("image2.png", "local/2022/1221/user211231q3wqeqwe.png", 2));

		assertDoesNotThrow(() -> this.feedPort.insertImage(this.imageInfos));
	}

	//todo 로그인 유무 check
	@Test
	@DisplayName("피드 작성 성공 : Image만 있음")
	void feed_write_success3() {
		this.userRepository.signUp(this.signUpForm);

		this.onlyImageInfos = List.of(
			new ImageUploadDto("image3.png", "local/2022/1221/user12q3wqeqwe.png", 1),
			new ImageUploadDto("image4.png", "local/2022/1221/user211231q3wqeqwe.png", 2));

		assertDoesNotThrow(() -> this.feedPort.insertImage(this.onlyImageInfos));
	}

	//todo 피드 작성 실패 : 유저가 로그인하지않음

	//todo 피드 작성 실패 : 유저가 존재하지않음

	//    @Test
	//    @DisplayName("피드 작성 실패: 유저가 존재하지않는다")
	//    void feed_write_fail_notExistUser() {
	//        assertThatThrownBy(() -> this.feedService.write(this.rightFeed))
	//                .isInstanceOf(UserNotFoundException.class);
	//    }

}
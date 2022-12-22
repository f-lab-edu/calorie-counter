package com.example.flabcaloriecounter.feed.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.feed.application.port.in.response.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserRepository;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserType;

@SpringBootTest
@Disabled
@Transactional
class FeedServiceTest {

	@Autowired
	FeedService feedService;

	@Autowired
	FeedPort feedPort;

	@Autowired
	UserRepository userRepository;

	FeedDto rightFeed;
	FeedDto wrongFeed;

	SignUpForm signUpForm;

	@BeforeEach
	void setup() {
		this.rightFeed = new FeedDto(
			"피드의 제목"
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

	@Test
	@DisplayName("피드 작성 성공 : contents만 있음 ")
	void feed_write_success() {
		this.userRepository.signUp(this.signUpForm);

		assertDoesNotThrow(() -> this.feedService.write(this.rightFeed, null));
	}
	//
	//    @Test
	//    @DisplayName("피드 작성 실패: 유저가 존재하지않는다")
	//    void feed_write_fail_notExistUser() {
	//        assertThatThrownBy(() -> this.feedService.write(this.rightFeed))
	//                .isInstanceOf(UserNotFoundException.class);
	//    }
}

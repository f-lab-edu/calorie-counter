package com.example.flabcaloriecounter.feed.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.response.CommentDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.util.StatusEnum;

@SpringBootTest
@Transactional
class FeedServiceTest {

	private static final String UPDATE_CONTENT = "닭가슴살을 먹었다(수정된내용)";

	@Autowired
	private FeedService feedService;

	@Autowired
	private FeedPort feedPort;

	@Autowired
	private UserService userService;

	private final SignUpForm wrongSignUpForm = new SignUpForm("otherUser1", "외부인", "asdf1234", "dudwls0505@daum.net");
	private final LoginForm wrongLoginForm = new LoginForm(wrongSignUpForm.getUserId(),
		wrongSignUpForm.getUserPassword());

	private final SignUpForm wrongSignUpForm2 = new SignUpForm("otherUser2", "외부인2", "asdf1234", "dudwls0504@daum.net");
	private final LoginForm wrongLoginForm2 = new LoginForm(wrongSignUpForm2.getUserId(),
		wrongSignUpForm2.getUserPassword());

	private final SignUpForm alreadySignUpForm = new SignUpForm("mockUser", "이영진", "asdf1234", "dudwls0505@naver.com");
	private final LoginForm alreadyLoginForm = new LoginForm("mockUser", "asdf1234");

	MockMultipartFile image1 = new MockMultipartFile("feedDto", "photos", "image/jpeg", "photos".getBytes());
	MockMultipartFile image2 = new MockMultipartFile("feedDto", "photos2", "image/jpeg", "photos2".getBytes());

	FeedDto notWriteFeed = new FeedDto("게시글내용1", List.of(this.image1, this.image2),
		alreadySignUpForm.getId());
	FeedDto feedWithContents = new FeedDto("게시글내용1", alreadySignUpForm.getId());
	FeedDto feedWithPhoto = new FeedDto(List.of(this.image1, this.image2), alreadySignUpForm.getId());

	@BeforeEach
	void setup() {
		this.userService.login(alreadyLoginForm);
		feedService.write(feedWithContents);
		feedService.write(feedWithPhoto);
		feedService.write(feedWithPhoto);
	}

	@Test
	@DisplayName("피드 수정 성공: 수정내용에 contents만 있음")
	void feed_update_success() {
		assertDoesNotThrow(() -> feedService.update(feedWithContents.getContents(), null, alreadySignUpForm.getId(),
			feedWithContents.getId()));
		assertThat(feedService.findByFeedId(feedWithContents.getId()).orElseThrow()).isEqualTo(
			new Feed(feedWithContents.getId(), feedWithContents.getContents(), null, alreadySignUpForm.getId(),
				null,
				null));
	}

	@Test
	@DisplayName("피드 수정 성공: 수정내용에 image만 있음")
	void feed_update_success2() {
		assertDoesNotThrow(
			() -> feedService.update(null, List.of(image1, image2), alreadySignUpForm.getId(),
				feedWithContents.getId()));
		//todo 이미지 수정확인..?
	}

	@Test
	@DisplayName("피드 수정 성공: 수정내용에 image, contents 둘다 있음")
	void feed_update_success3() {
		assertDoesNotThrow(
			() -> feedService.update(UPDATE_CONTENT, List.of(image1, image2), alreadySignUpForm.getId(),
				feedWithContents.getId()));

		//todo 이미지 수정확인..?
	}

	@Test
	@DisplayName("피드 삭제 성공")
	void feed_delete_success() {
		assertDoesNotThrow(() -> feedService.delete(alreadySignUpForm.getId(), feedWithContents.getId()));
		assertThat(feedService.findByFeedId(feedWithContents.getId())).isEqualTo(Optional.empty());
		assertThat(feedPort.findImageByFeedId(feedWithContents.getId()).size()).isEqualTo(0);
	}

	@Test
	@DisplayName("피드 조회 성공")
	void feed_read_success() {
		List<FeedListDto> feedList = feedService.getFeedList(new Paging(feedService.maxCursor(), 5));

		//then
		assertThat(feedList.size()).isEqualTo(3);
		assertThat(feedService.feedListWithPhoto(feedList, alreadySignUpForm.getId(), 1, 30).size()).isEqualTo(3);
		assertThat(feedService.feedListWithPhoto(feedList, alreadySignUpForm.getId(), 1, 30).get(0)).isEqualTo(
			new GetFeedListDto(
				feedList.get(0).feedId(),
				feedList.get(0).contents(),
				feedList.get(0).writeDate(),
				feedList.get(0).userId(),
				feedPort.photos(feedList.get(0).feedId()),
				feedService.likeCount(feedList.get(0).feedId()),
				feedPort.findLikeStatusByUserId(feedList.get(0).feedId(), alreadySignUpForm.getId()),
				CommentDto.createSequence(feedPort.comment(feedList.get(0).feedId(), 0, 30), 0)
			));

		assertThat(feedService.feedListWithPhoto(feedList, alreadySignUpForm.getId(), 1, 30).get(2)).isEqualTo(
			new GetFeedListDto(
				feedList.get(2).feedId(),
				feedList.get(2).contents(),
				feedList.get(2).writeDate(),
				feedList.get(2).userId(),
				feedPort.photos(feedList.get(2).feedId()),
				feedService.likeCount(feedList.get(2).feedId()),
				feedPort.findLikeStatusByUserId(feedList.get(2).feedId(), alreadySignUpForm.getId()),
				CommentDto.createSequence(feedPort.comment(feedList.get(0).feedId(), 0, 30), 0)
			));
	}

	@Nested
	@DisplayName("작성된 피드가 없는경우")
	class FeedNotExistBlock {

		@Nested
		@DisplayName("피드 작성케이스")
		class FeedWriteBlock {

			@Test
			@DisplayName("피드 작성 성공 : contents만 있음 ")
			void feed_write_success() {
				//when
				assertDoesNotThrow(() -> feedService.write(notWriteFeed));

				assertThat(feedService.findByFeedId(notWriteFeed.getId())).isEqualTo(
					Optional.of(new Feed(notWriteFeed.getId(), notWriteFeed.getContents(), null,
						alreadySignUpForm.getId(), null, null)));
			}

			@Test
			@DisplayName("피드 작성 성공 : contents, Image 둘다 있음")
			void feed_write_success2() {
				//when
				assertDoesNotThrow(() -> feedService.write(notWriteFeed));

				List<ImageUploadDto> imageInfos = List.of(
					new ImageUploadDto("image1.png", "local/2022/1221/user12q3wqeqwe.png",
						notWriteFeed.getId()),
					new ImageUploadDto("image2.png", "local/2022/1221/user211231q3wqeqwe.png",
						notWriteFeed.getId()));

				assertDoesNotThrow(() -> feedPort.insertImage(imageInfos));
				assertThat(feedService.findByFeedId(notWriteFeed.getId())).isEqualTo(
					Optional.of(new Feed(notWriteFeed.getId(), notWriteFeed.getContents(), null,
						alreadySignUpForm.getId(), null, null)));
			}

			@Test
			@DisplayName("피드 작성 성공 : Image만 있음")
			void feed_write_success3() {
				//when
				assertDoesNotThrow(() -> feedService.write(feedWithPhoto));

				List<ImageUploadDto> onlyImageInfos = List.of(
					new ImageUploadDto("image3.png", "local/2022/1221/user12q3wqeqwe.png", feedWithPhoto.getId()),
					new ImageUploadDto("image4.png", "local/2022/1221/user211231q3wqeqwe.png",
						feedWithPhoto.getId()));

				assertDoesNotThrow(() -> feedPort.insertImage(onlyImageInfos));
				assertThat(feedService.findByFeedId(feedWithPhoto.getId())).isEqualTo(
					Optional.of(
						new Feed(feedWithPhoto.getId(), feedWithPhoto.getContents(), null, alreadySignUpForm.getId(),
							null,
							null)));
			}
		}

		@Test
		@DisplayName("피드 수정 실패: 존재하지 않는 피드")
		void feed_update_fail() {
			CustomException customException = assertThrows(CustomException.class,
				() -> feedService.update(UPDATE_CONTENT, null, alreadySignUpForm.getId(), notWriteFeed.getId()));
			assertThat(StatusEnum.FEED_NOT_FOUND).isEqualTo(customException.getStatusEnum());
		}

		@Test
		@DisplayName("피드 삭제 실패: 존재하지 않는 피드")
		void feed_delete_fail() {
			CustomException customException = assertThrows(CustomException.class,
				() -> feedService.delete(alreadySignUpForm.getId(), notWriteFeed.getId()));
			assertThat(StatusEnum.FEED_NOT_FOUND).isEqualTo(customException.getStatusEnum());
		}
	}

	@Nested
	@DisplayName("권한이 없는유저인 경우")
	class wrongUserBlock {

		@Test
		@DisplayName("피드 수정 실패: 권한이 없는 유저")
		void feed_update_fail3() {
			//given
			userService.signUp(wrongSignUpForm);
			userService.login(wrongLoginForm);

			System.out.println("wrong =" + wrongSignUpForm.getId());
			System.out.println("already =" + alreadySignUpForm.getId());

			CustomException customException = assertThrows(CustomException.class,
				() -> feedService.update("수정할 내용", null, wrongSignUpForm.getId(), feedWithContents.getId()));
			assertThat(StatusEnum.INVALID_USER).isEqualTo(customException.getStatusEnum());
		}

		@Test
		@DisplayName("피드 삭제 실패: 권한이 없는 유저")
		void feed_delete_fail3() {
			//given
			userService.signUp(wrongSignUpForm2);
			userService.login(wrongLoginForm2);

			System.out.println("wrong2 =" + wrongSignUpForm2.getId());
			System.out.println("already2 =" + alreadySignUpForm.getId());

			CustomException customException = assertThrows(CustomException.class,
				() -> feedService.delete(wrongSignUpForm2.getId(), feedWithContents.getId()));
			assertThat(StatusEnum.INVALID_USER).isEqualTo(customException.getStatusEnum());
		}
	}
}
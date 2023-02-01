package com.example.flabcaloriecounter.feed.application.service;

import static com.example.flabcaloriecounter.user.source.TestFeedSource.contentsFeed;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightSignUpForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongSignUpForm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.GetFeedListDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.Paging;
import com.example.flabcaloriecounter.feed.application.port.in.response.CommentDto;
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.domain.Feed;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.util.StatusEnum;

@SpringBootTest
@Transactional
@Sql("classpath:tableInit.sql")
class FeedServiceTest {

	private static final String WRITE_CONTENT = "닭가슴살을 먹었다";
	private static final String UPDATE_CONTENT = "닭가슴살을 먹었다(수정된내용)";

	@Autowired
	private FeedService feedService;

	@Autowired
	private FeedPort feedPort;

	@Autowired
	private UserService userService;

	private FeedRequestDto contentsAndPhotoFeed;

	MockMultipartFile image1;
	MockMultipartFile image2;

	@BeforeEach
	void setup() {
		this.image1 = new MockMultipartFile(
			"feedDto",
			"photos",
			"image/jpeg",
			"photos".getBytes()
		);

		this.image2 = new MockMultipartFile(
			"feedDto",
			"photos2",
			"image/jpeg",
			"photos2".getBytes()
		);

		this.contentsAndPhotoFeed = new FeedRequestDto(
			WRITE_CONTENT,
			List.of(this.image1, this.image2)
		);
	}

	@Test
	@DisplayName("피드 작성 성공 : contents만 있음 ")
	void feed_write_success() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);

		assertDoesNotThrow(() -> this.feedService.write(contentsFeed, rightLoginForm.userId()));
		assertThat(this.feedService.findByFeedId(1)).isEqualTo(
			Optional.of(new Feed(1, contentsFeed.contents(), null, 1, null, null)));
	}

	@Test
	@DisplayName("피드 작성 성공 : contents, Image 둘다 있음")
	void feed_write_success2() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		List<ImageUploadDto> imageInfos = List.of(
			new ImageUploadDto("image1.png", "local/2022/1221/user12q3wqeqwe.png",
				this.feedPort.write(WRITE_CONTENT, 1)),
			new ImageUploadDto("image2.png", "local/2022/1221/user211231q3wqeqwe.png",
				this.feedPort.write(WRITE_CONTENT, 1)));

		assertDoesNotThrow(() -> this.feedPort.insertImage(imageInfos));
		assertThat(this.feedService.findByFeedId(1)).isEqualTo(
			Optional.of(new Feed(1, WRITE_CONTENT, null, 1, null, null)));
	}

	@Test
	@DisplayName("피드 작성 성공 : Image만 있음")
	void feed_write_success3() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		List<ImageUploadDto> onlyImageInfos = List.of(
			new ImageUploadDto("image3.png", "local/2022/1221/user12q3wqeqwe.png", this.feedPort.write("", 1)),
			new ImageUploadDto("image4.png", "local/2022/1221/user211231q3wqeqwe.png", this.feedPort.write("", 1)));

		assertDoesNotThrow(() -> this.feedPort.insertImage(onlyImageInfos));
		assertThat(this.feedService.findByFeedId(1)).isEqualTo(
			Optional.of(new Feed(1, "", null, 1, null, null)));
	}

	@Test
	@DisplayName("피드 작성 실패: 유저가 존재하지않는다")
	void feed_write_fail_notExistUser() {
		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.write(contentsFeed, "notExist"));
		assertThat(StatusEnum.USER_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("피드 수정 성공: contents만 있음")
	void feed_update_success() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		assertDoesNotThrow(() -> this.feedService.update(UPDATE_CONTENT, null, rightLoginForm.userId(), 1));
		assertThat(this.feedService.findByFeedId(1).orElseThrow()).isEqualTo(
			new Feed(1, UPDATE_CONTENT, null, 1, null, null));
	}

	@Test
	@DisplayName("피드 수정 성공: image만 있음")
	void feed_update_success2() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		assertDoesNotThrow(
			() -> this.feedService.update(null, List.of(this.image1, this.image2), rightLoginForm.userId(), 1));
	}

	@Test
	@DisplayName("피드 수정 성공: image, contents둘다 있음")
	void feed_update_success3() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());

		assertDoesNotThrow(
			() -> this.feedService.update(UPDATE_CONTENT, List.of(image1, image2), rightLoginForm.userId(), 1));
	}

	@Test
	@DisplayName("피드 수정 실패: 존재하지 않는 피드")
	void feed_update_fail() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);

		assertThatThrownBy(() -> this.feedService.update(UPDATE_CONTENT, null, rightLoginForm.userId(), 1));
	}

	@Test
	@DisplayName("피드 수정 실패: 존재하지 않는 유저")
	void feed_update_fail2() {
		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.update(UPDATE_CONTENT, null, "user", 1));
		assertThat(StatusEnum.USER_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("피드 수정 실패: 권한이 없는 유저")
	void feed_update_fail3() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.signUp(wrongSignUpForm);
		this.userService.login(rightLoginForm);
		this.userService.login(wrongLoginForm);
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());

		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.update(UPDATE_CONTENT, null, wrongLoginForm.userId(), 1));
		assertThat(StatusEnum.INVALID_USER).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("피드 삭제 성공")
	void feed_delete_success() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());

		assertDoesNotThrow(() -> this.feedService.delete(rightLoginForm.userId(), 1));
		assertThat(this.feedService.findByFeedId(1)).isEqualTo(Optional.empty());
		assertThat(this.feedPort.findImageByFeedId(1).size()).isEqualTo(0);
	}

	@Test
	@DisplayName("피드 삭제 실패: 존재하지 않는 피드")
	void feed_delete_fail() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);

		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.delete(rightLoginForm.userId(), 1));
		assertThat(StatusEnum.FEED_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("피드 삭제 실패: 존재하지 않는 유저")
	void feed_delete_fail2() {
		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.delete(rightLoginForm.userId(), 1));
		assertThat(StatusEnum.USER_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("피드 삭제 실패: 권한이 없는 유저")
	void feed_delete_fail3() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.signUp(wrongSignUpForm);
		this.userService.login(rightLoginForm);
		this.userService.login(wrongLoginForm);
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());

		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.delete(wrongLoginForm.userId(), 1));
		assertThat(StatusEnum.INVALID_USER).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("피드 조회 성공")
	void feed_read_success() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());

		List<FeedListDto> feedList = this.feedService.getFeedList(new Paging(5, 5));

		//then
		assertThat(feedList.size()).isEqualTo(3);
		assertThat(this.feedService.feedListWithPhoto(feedList, 1, 1, 30).size()).isEqualTo(3);
		assertThat(this.feedService.feedListWithPhoto(feedList, 1, 1, 30).get(0)).isEqualTo(new GetFeedListDto(
			feedList.get(0).feedId(),
			feedList.get(0).contents(),
			feedList.get(0).writeDate(),
			feedList.get(0).userId(),
			this.feedPort.photos(feedList.get(0).feedId()),
			this.feedService.likeCount(feedList.get(0).feedId()),
			this.feedPort.findLikeStatusByUserId(feedList.get(0).feedId(), 1),
			CommentDto.createSequence(this.feedPort.comment(feedList.get(0).feedId(), 0, 30), 0)
		));

		assertThat(this.feedService.feedListWithPhoto(feedList, 1, 1, 30).get(2)).isEqualTo(new GetFeedListDto(
			feedList.get(2).feedId(),
			feedList.get(2).contents(),
			feedList.get(2).writeDate(),
			feedList.get(2).userId(),
			this.feedPort.photos(feedList.get(2).feedId()),
			this.feedService.likeCount(feedList.get(2).feedId()),
			this.feedPort.findLikeStatusByUserId(feedList.get(2).feedId(), 1),
			CommentDto.createSequence(this.feedPort.comment(feedList.get(0).feedId(), 0, 30), 0)
		));
	}

	@Test
	@DisplayName("maxCursor : 가장 큰 피드ID값을 가져온다")
	void feed_read_success2() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());
		this.feedService.write(this.contentsAndPhotoFeed, rightLoginForm.userId());

		assertThat(this.feedService.maxCursor()).isEqualTo(3);
	}

	@Test
	@DisplayName("좋아요 성공: 최초로 좋아요누르는경우")
	void feed_like_success1() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		//when
		assertThat(this.feedPort.likeCount(1, LikeStatus.ACTIVATE)).isEqualTo(0);
		assertThat(this.feedPort.likeCount(1, LikeStatus.NOT_ACTIVATE)).isEqualTo(0);
		this.feedService.like(1, rightLoginForm.userId());

		//then
		assertThat(this.feedPort.findLikeStatusByUserId(1, 1)).isEqualTo(LikeStatus.ACTIVATE);
		assertThat(this.feedPort.likeCount(1, LikeStatus.ACTIVATE)).isEqualTo(1);
	}

	@Test
	@DisplayName("좋아요 성공: 비활성화 상태에서 다시 누르는경우")
	void feed_like_success2() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.like(1, rightLoginForm.userId());
		this.feedPort.changeStatus(1, 1, LikeStatus.NOT_ACTIVATE);

		//when
		assertThat(this.feedPort.likeCount(1, LikeStatus.ACTIVATE)).isEqualTo(0);
		assertThat(this.feedPort.likeCount(1, LikeStatus.NOT_ACTIVATE)).isEqualTo(1);
		this.feedService.like(1, rightLoginForm.userId());

		//then
		assertThat(this.feedPort.findLikeStatusByUserId(1, 1)).isEqualTo(LikeStatus.ACTIVATE);
		assertThat(this.feedPort.likeCount(1, LikeStatus.ACTIVATE)).isEqualTo(1);
		assertThat(this.feedPort.likeCount(1, LikeStatus.NOT_ACTIVATE)).isEqualTo(0);
	}

	@Test
	@DisplayName("좋아요 성공: 활성화 상태에서 누르는경우")
	void feed_like_success3() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.like(1, rightLoginForm.userId());

		//when
		assertThat(this.feedPort.likeCount(1, LikeStatus.ACTIVATE)).isEqualTo(1);
		assertThat(this.feedPort.likeCount(1, LikeStatus.NOT_ACTIVATE)).isEqualTo(0);
		this.feedService.like(1, rightLoginForm.userId());

		//then
		assertThat(this.feedPort.findLikeStatusByUserId(1, 1)).isEqualTo(LikeStatus.NOT_ACTIVATE);
		assertThat(this.feedPort.likeCount(1, LikeStatus.ACTIVATE)).isEqualTo(0);
		assertThat(this.feedPort.likeCount(1, LikeStatus.NOT_ACTIVATE)).isEqualTo(1);
	}

	@Test
	@DisplayName("좋아요 실패: 존재하지 않는 피드")
	void feed_like_fail1() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);

		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.like(1, rightLoginForm.userId()));
		assertThat(StatusEnum.FEED_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("좋아요 실패: 존재하지 않는 유저")
	void feed_like_fail2() {
		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.like(1, "없는유저"));
		assertThat(StatusEnum.USER_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("좋아요 개수 성공")
	void feed_likeCount_success1() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.userService.signUp(wrongSignUpForm);
		this.userService.login(rightLoginForm);
		this.userService.login(wrongLoginForm);

		//when
		this.feedService.like(1, rightLoginForm.userId());
		this.feedService.like(1, wrongLoginForm.userId());

		//then
		assertThat(this.feedService.likeCount(1)).isEqualTo(2);
	}

	@Test
	@DisplayName("좋아요 개수 실패: 존재하지 않는 피드")
	void feed_likeCount_fail1() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);

		CustomException customException = assertThrows(CustomException.class,
			() -> this.feedService.likeCount(1));
		assertThat(StatusEnum.FEED_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}
}
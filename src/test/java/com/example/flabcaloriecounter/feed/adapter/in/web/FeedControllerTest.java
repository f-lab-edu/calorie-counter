package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.user.source.TestFeedSource.contents;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.contentsAndPhotoFeed;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.contentsFeed;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.mockFeedContent;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.mockImage1;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.mockImage2;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightSignUpForm;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.application.service.FeedService;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
import com.example.flabcaloriecounter.user.application.port.in.UserUseCase;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseToken;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("classpath:tableInit.sql")
class FeedControllerTest {

	private static final String NOT_AUTHORIZED_USER = "인증되지 않은 유저입니다.";
	private static final String NOT_EXIST_FEED_MSG = "존재하지 않는 피드입니다.";
	private static final String EMPTY_FEED_MSG = "피드 내용이 비어있습니다";
	public static final String NOT_TOKEN_HEADER = "요청헤더에 토큰이 없습니다.";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserUseCase userUseCase;

	@Autowired
	private FeedService feedService;

	@Autowired
	private FeedPort feedPort;

	@Test
	@DisplayName("피드 작성 성공 : 이미지, 내용 둘다 있는경우")
	void feed_write_test() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart("/feeds")
				.file(mockFeedContent)
				.file(mockImage1)
				.file(mockImage2)
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 작성 성공 : 이미지만 있는경우")
	void feed_write_test2() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart("/feeds")
				.file(mockImage1)
				.file(mockImage2)
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 작성 성공 : 내용만 있는경우")
	void feed_write_test3() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart("/feeds")
				.file(mockFeedContent)
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 작성 실패 : 내용, 이미지 모두 비어있는경우")
	void feed_write_test4() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart("/feeds")
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(EMPTY_FEED_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 작성 실패 : 로그인 하지 않은 회원인경우")
	void feed_write_test5() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);

		this.mockMvc.perform(multipart("/feeds")
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("message").value(NOT_TOKEN_HEADER))
			.andExpect(jsonPath("statusCode").value("UNAUTHORIZED"))
			.andDo(print());
	}

	@Test
	@DisplayName("인가 실패 : 요청헤더에 토큰이 없음")
	void authorize_test() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart("/feeds")
				.file(mockFeedContent)
				.file(mockImage1)
				.file(mockImage2)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(jsonPath("message").value("요청헤더에 토큰이 없습니다."))
			.andExpect(jsonPath("statusCode").value("UNAUTHORIZED"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("피드 수정 성공: image, contents 둘다 있음")
	void feed_update_test() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.file(contents)
				.file(mockImage1)
				.file(mockImage1)
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 성공: contents만 있음")
	void feed_update_test2() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.file(contents)
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 성공: image만 있음")
	void feed_update_test3() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.file(mockImage1)
				.file(mockImage2)
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 실패 : 내용, 이미지 모두 비어있는경우")
	void feed_update_test4() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(EMPTY_FEED_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 수정 실패 : 로그인하지 않은 경우")
	void feed_update_test5() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("message").value(NOT_TOKEN_HEADER))
			.andExpect(jsonPath("statusCode").value("UNAUTHORIZED"))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 삭제 성공 : 로그인 한 유저")
	void feed_delete_success() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		this.mockMvc.perform(delete("/feeds/1")
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 삭제 실패 : 로그인 하지않은 유저")
	void feed_delete_fail() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);

		this.mockMvc.perform(delete("/feeds/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("message").value(NOT_TOKEN_HEADER))
			.andExpect(jsonPath("statusCode").value("UNAUTHORIZED"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("피드 삭제 실패: 피드가 존재하지않음")
	void feed_delete_fail3() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(delete("/feeds/1")
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("피드 조회 성공: 좋아요개수,좋아요상태, 사진, 글내용")
	void feed_read_success() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.like(1, rightSignUpForm.userId());

		this.mockMvc.perform(get("/feeds?cursorNo=4&displayPerPage=3")
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)))
			.andExpect(jsonPath("$[2].id").value(1))
			.andExpect(jsonPath("$[2].contents").value(contentsAndPhotoFeed.contents()))
			.andExpect(jsonPath("$[2].userId").value(1))
			.andExpect(jsonPath("$[2].photos.length()", is(2)))
			.andExpect(jsonPath("$[2].likeCount").value(1))
			.andExpect(jsonPath("$[2].likeStatus").value("ACTIVATE"))
			.andExpect(jsonPath("$[0].id").value(3))
			.andExpect(jsonPath("$[0].contents").value(contentsFeed.contents()))
			.andExpect(jsonPath("$[0].userId").value(1))
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("피드 조회 성공: cursor가 0보다 작으면 maxId값을 준다.")
	void feed_read_fail() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());

		this.mockMvc.perform(get("/feeds?cursorNo=-1&displayPerPage=3")
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)))
			.andExpect(jsonPath("$[0].id").value(3))
			.andExpect(jsonPath("$[0].contents").value(contentsAndPhotoFeed.contents()))
			.andExpect(jsonPath("$[0].userId").value(1))

			.andExpect(jsonPath("$[2].id").value(1))
			.andExpect(jsonPath("$[2].contents").value(contentsFeed.contents()))
			.andExpect(jsonPath("$[2].userId").value(1))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 좋아요 취소 성공")
	void feed_like_success2() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());

		this.feedPort.like(1, 1, LikeStatus.ACTIVATE);

		this.mockMvc.perform(post("/feeds/1/like")
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 좋아요 실패: 존재하지 않는 게시물")
	void feed_like_fail2() throws Exception {
		this.userUseCase.signUp(rightSignUpForm);
		ResponseToken responseToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(post("/feeds/1/like")
				.header("Authorization", responseToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("message").value(NOT_EXIST_FEED_MSG))
			.andExpect(jsonPath("statusCode").value("NOT_FOUND"))
			.andDo(print())
			.andExpect(status().isNotFound());
	}
}
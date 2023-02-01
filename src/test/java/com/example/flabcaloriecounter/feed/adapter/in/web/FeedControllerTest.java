package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.user.source.TestFeedSource.commentDto;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.contents;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.contentsAndPhotoFeed;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.contentsFeed;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.mockFeedContent;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.mockImage1;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.mockImage2;
import static com.example.flabcaloriecounter.user.source.TestFeedSource.reply;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightSignUpForm;
import static com.example.flabcaloriecounter.util.CustomResponse.ERROR;
import static com.example.flabcaloriecounter.util.CustomResponse.SUCCESS;
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
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.util.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("classpath:tableInit.sql")
class FeedControllerTest {

	private final String FEEDS_1_LIKE_URL = "/feeds/1/like";
	private final String COMMENT_POST_URL = "/feeds/1/comment";
	private final String FEED_WRITE_URL = "/feeds";
	private final String AUTHORIZATION_HEADER = "Authorization";
	private final String AUTHORIZATION_BEARER = "Bearer ";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserUseCase userUseCase;

	@Autowired
	private FeedService feedService;

	@Autowired
	private FeedPort feedPort;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("피드 작성 성공 : 이미지, 내용 둘다 있는경우")
	void feed_write_test() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart(FEED_WRITE_URL)
				.file(mockFeedContent)
				.file(mockImage1)
				.file(mockImage2)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 작성 성공 : 이미지만 있는경우")
	void feed_write_test2() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart(FEED_WRITE_URL)
				.file(mockImage1)
				.file(mockImage2)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 작성 성공 : 내용만 있는경우")
	void feed_write_test3() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart(FEED_WRITE_URL)
				.file(mockFeedContent)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 작성 실패 : 내용, 이미지 모두 비어있는경우")
	void feed_write_test4() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(multipart(FEED_WRITE_URL)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_FEED.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 작성 실패 : 로그인 하지 않은 회원인경우")
	void feed_write_test5() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);

		this.mockMvc.perform(multipart(FEED_WRITE_URL)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_TOKEN.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 수정 성공: image, contents 둘다 있음")
	void feed_update_test() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
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
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 성공: contents만 있음")
	void feed_update_test2() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.file(contents)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 성공: image만 있음")
	void feed_update_test3() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.file(mockImage1)
				.file(mockImage2)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 실패 : 내용, 이미지 모두 비어있는경우")
	void feed_update_test4() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_FEED.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 수정 실패 : 로그인하지 않은 경우")
	void feed_update_test5() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_TOKEN.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 삭제 성공 : 로그인 한 유저")
	void feed_delete_success() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		this.mockMvc.perform(delete("/feeds/1")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
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
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_TOKEN.getMessage()))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("피드 삭제 실패: 피드가 존재하지않음")
	void feed_delete_fail3() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(delete("/feeds/1")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("피드 조회 성공: 좋아요개수,좋아요상태, 사진, 글내용,댓글")
	void feed_read_success() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.like(1, rightSignUpForm.userId());
		this.feedService.comment(1, 1, "댓글1");
		this.feedService.comment(1, 1, "댓글2");
		this.feedService.comment(1, 1, "댓글3");
		this.feedService.comment(1, 1, "댓글4");

		this.mockMvc.perform(get("/feeds?cursorNo=4&displayPerPage=3&commentPageNum=1&commentPerPage=3")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(SUCCESS))
			.andExpect(jsonPath("$.info.length()", is(3)))
			.andExpect(jsonPath("$.info[2].id").value(1))
			.andExpect(jsonPath("$.info[2].contents").value(contentsAndPhotoFeed.contents()))
			.andExpect(jsonPath("$.info[2].userId").value(1))
			.andExpect(jsonPath("$.info[2].photos.length()", is(2)))
			.andExpect(jsonPath("$.info[2].likeCount").value(1))
			.andExpect(jsonPath("$.info[2].likeStatus").value("ACTIVATE"))
			.andExpect(jsonPath("$.info[2].comments.length()", is(3)))
			.andExpect(jsonPath("$.info[0].id").value(3))
			.andExpect(jsonPath("$.info[0].contents").value(contentsFeed.contents()))
			.andExpect(jsonPath("$.info[0].userId").value(1))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 조회 성공: cursor가 0보다 작으면 maxId값을 준다.")
	void feed_read_fail() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());

		this.mockMvc.perform(get("/feeds?cursorNo=-1&displayPerPage=3")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.info.length()", is(3)))
			.andExpect(jsonPath("$.info[0].id").value(3))
			.andExpect(jsonPath("$.info[0].contents").value(contentsAndPhotoFeed.contents()))
			.andExpect(jsonPath("$.info[0].userId").value(1))

			.andExpect(jsonPath("$.info[2].id").value(1))
			.andExpect(jsonPath("$.info[2].contents").value(contentsFeed.contents()))
			.andExpect(jsonPath("$.info[2].userId").value(1))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 좋아요 취소 성공")
	void feed_like_success2() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.write(contentsAndPhotoFeed, rightLoginForm.userId());
		this.feedPort.like(1, 1, LikeStatus.ACTIVATE);

		this.mockMvc.perform(post(FEEDS_1_LIKE_URL)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 좋아요 실패: 존재하지 않는 게시물")
	void feed_like_fail2() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(post(FEEDS_1_LIKE_URL)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.FEED_NOT_FOUND.getMessage()))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("피드 좋아요 실패: 로그인 하지않은 유저")
	void feed_like_fail3() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);

		this.mockMvc.perform(post(FEEDS_1_LIKE_URL)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_TOKEN.getMessage()))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("댓글 작성 성공")
	void feed_comment_success() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		this.mockMvc.perform(post(COMMENT_POST_URL)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.content(objectMapper.writeValueAsString(commentDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(SUCCESS))
			.andExpect(jsonPath("info.commentId").value(commentDto.getId()))
			.andExpect(jsonPath("info.contents").value(commentDto.getContents()))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("댓글 작성 실패: 게시글이 존재하지않음")
	void feed_comment_fail() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(post(COMMENT_POST_URL)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.content(objectMapper.writeValueAsString(commentDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.FEED_NOT_FOUND.getMessage()))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("댓글 작성 실패: 로그인 하지않은 유저")
	void feed_comment_fail2() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);

		this.mockMvc.perform(post(COMMENT_POST_URL)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_TOKEN.getMessage()))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("대댓글 작성 성공")
	void feed_reply_success() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());
		this.feedService.comment(1, 1, commentDto.getContents());

		//then
		this.mockMvc.perform(post(COMMENT_POST_URL)
				.param("parentId", String.valueOf(1L))
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.content(
					objectMapper.writeValueAsString(reply))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(SUCCESS))
			.andExpect(jsonPath("info.commentId").value(reply.getId()))
			.andExpect(jsonPath("info.contents").value(reply.getContents()))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("대댓글 작성 실패: 게시글이 존재하지않음")
	void feed_reply_fail() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);

		this.mockMvc.perform(post(COMMENT_POST_URL)
				.param("parentId", String.valueOf(1L))
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.content(objectMapper.writeValueAsString(commentDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.FEED_NOT_FOUND.getMessage()))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("대댓글 작성 실패: 부모댓글이 존재하지않음")
	void feed_reply_fail2() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);
		ResponseIssuedToken responseIssuedToken = this.userUseCase.login(rightLoginForm);
		this.feedService.write(contentsFeed, rightLoginForm.userId());

		this.mockMvc.perform(post(COMMENT_POST_URL)
				.param("parentId", String.valueOf(1L))
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.content(objectMapper.writeValueAsString(commentDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.COMMENT_NOT_FOUND.getMessage()))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("대댓글 작성 실패: 로그인 하지않은 유저")
	void feed_reply_fail3() throws Exception {
		//given
		this.userUseCase.signUp(rightSignUpForm);

		this.mockMvc.perform(post(COMMENT_POST_URL)
				.param("parentId", String.valueOf(1L))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_TOKEN.getMessage()))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}
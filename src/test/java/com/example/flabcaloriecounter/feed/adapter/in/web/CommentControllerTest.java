package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.feed.adapter.in.web.FeedControllerTest.AUTHORIZATION_BEARER;
import static com.example.flabcaloriecounter.feed.adapter.in.web.FeedControllerTest.AUTHORIZATION_HEADER;
import static com.example.flabcaloriecounter.util.CustomResponse.ERROR;
import static com.example.flabcaloriecounter.util.CustomResponse.SUCCESS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.service.FeedService;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.util.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private FeedService feedService;

	@Autowired
	private ObjectMapper objectMapper;

	private ResponseIssuedToken responseIssuedToken;

	private final SignUpForm wrongSignUpForm = new SignUpForm("wrongUser", "김영진", "asdf1234", "dudwls0505@nate.com");
	private final SignUpForm alreadySignUpForm = new SignUpForm("mockUser", "이영진", "asdf1234", "dudwls0505@naver.com");
	private final LoginForm alreadyLoginForm = new LoginForm("mockUser", "asdf1234");
	LoginForm wrongLoginForm = new LoginForm(wrongSignUpForm.getUserId(), wrongSignUpForm.getUserPassword());

	FeedDto feedWithContents = new FeedDto("게시글내용1", alreadySignUpForm.getId());

	CommentRequestDto comment = new CommentRequestDto("댓글1");
	CommentRequestDto replyDto = new CommentRequestDto("답글1", comment.getParentId());
	// todo 부모ID를 가진 댓글이 없는 가정인데, 부모ID에 어떤값을 가정해서 줘야할까?
	CommentRequestDto replyWithNotParent = new CommentRequestDto("답글1", Long.MAX_VALUE);

	@BeforeEach
	void setup() {
		this.responseIssuedToken = this.userService.login(alreadyLoginForm);
	}

	@Nested
	@DisplayName("피드가 존재하는 경우")
	class FeedExistBlock {

		@BeforeEach
		void setup() {
			feedService.write(feedWithContents);
		}

		@Test
		@DisplayName("댓글 작성 성공")
		void feed_comment_success() throws Exception {
			mockMvc.perform(post("/feeds/" + feedWithContents.getId() + "/comment")
					.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
					.content(objectMapper.writeValueAsString(comment))
					.contentType(APPLICATION_JSON))
				.andExpect(jsonPath("result").value(SUCCESS))
				.andExpect(jsonPath("info.commentId").exists())
				.andExpect(jsonPath("info.contents").value(comment.getContents()))
				.andDo(print())
				.andExpect(status().isCreated());
		}

		@Test
		@DisplayName("대댓글 작성 성공: 부모댓글이 존재한다")
		void feed_reply_success() throws Exception {
			//given : 부모댓글
			feedService.comment(feedWithContents.getId(), alreadySignUpForm.getId(), comment);

			mockMvc.perform(post("/feeds/" + feedWithContents.getId() + "/comment")
					.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
					.content(
						objectMapper.writeValueAsString(replyDto))
					.contentType(APPLICATION_JSON))
				.andExpect(jsonPath("result").value(SUCCESS))
				.andExpect(jsonPath("info.commentId").exists())
				.andExpect(jsonPath("info.contents").value(replyDto.getContents()))
				.andDo(print())
				.andExpect(status().isCreated());
		}

		@Test
		@DisplayName("대댓글 작성 실패: 부모댓글이 존재하지않음")
		void feed_reply_fail2() throws Exception {
			mockMvc.perform(post("/feeds/" + feedWithContents.getId() + "/comment")
					.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
					.content(objectMapper.writeValueAsString(replyWithNotParent))
					.contentType(APPLICATION_JSON))
				.andExpect(jsonPath("result").value(ERROR))
				.andExpect(jsonPath("errorMessage").value(StatusEnum.COMMENT_NOT_FOUND.getMessage()))
				.andDo(print())
				.andExpect(status().isNotFound());
		}

	}

	@Test
	@DisplayName("댓글 작성 실패: 피드가 존재하지않음")
	void feed_comment_fail() throws Exception {
		mockMvc.perform(post("/feeds/" + feedWithContents.getId() + "/comment")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.content(objectMapper.writeValueAsString(comment))
				.contentType(APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.FEED_NOT_FOUND.getMessage()))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("대댓글 작성 실패: 피드가 존재하지않음")
	void feed_reply_fail() throws Exception {
		mockMvc.perform(post("/feeds/" + feedWithContents.getId() + "/comment")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.content(objectMapper.writeValueAsString(replyDto))
				.contentType(APPLICATION_JSON))
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.FEED_NOT_FOUND.getMessage()))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

}

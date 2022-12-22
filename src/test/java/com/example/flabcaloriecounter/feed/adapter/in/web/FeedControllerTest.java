package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.EMPTY_FEED_MSG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.service.FeedService;
import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class FeedControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Autowired
	private FeedService feedService;

	@Autowired
	private ObjectMapper objectMapper;

	SignUpForm rightUserForm;
	private FeedDto contentsFeed;

	MockMultipartFile image1;
	MockMultipartFile contents;
	MockMultipartFile nullContents;
	MockMultipartFile image2;

	record FeedTestDto(String contents) {
	}

	@BeforeEach
	void setup() throws JsonProcessingException {
		rightUserForm = new SignUpForm(
			"mockUser",
			"올바른유저",
			"12345678",
			"dudwls0505@naver.com",
			60.03,
			UserType.ORDINARY,
			JudgeStatus.getInitialJudgeStatusByUserType(UserType.ORDINARY)
		);

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

		this.contents = new MockMultipartFile(
			"feedDto",
			"feedDto",
			"application/json",
			objectMapper.writeValueAsString(new FeedTestDto("닭가슴살을 먹었다")).getBytes()
		);

		this.nullContents = new MockMultipartFile(
			"feedDto",
			"feedDto",
			"application/json",
			objectMapper.writeValueAsString(new FeedTestDto("")).getBytes()
		);

		this.contentsFeed = new FeedDto(
			"닭가슴살을 먹었다",
			null
		);
	}

	//todo 로그인한 유저여야한다
	@Test
	@DisplayName("피드 작성 성공 : 이미지, 내용 둘다 있는경우")
	void feed_write_test() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.file(this.contents)
				.file(this.image1)
				.file(this.image2)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo 로그인한 유저여야한다
	@Test
	@DisplayName("피드 작성 성공 : 이미지만 있는경우")
	void feed_write_test2() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.file(this.nullContents)
				.file(this.image1)
				.file(this.image2)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo 로그인한 유저여야한다
	@Test
	@DisplayName("피드 작성 성공 : 내용만 있는경우")
	void feed_write_test3() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.file(contents)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo 로그인한 유저여야한다
	@Test
	@DisplayName("피드 작성 실패 : 내용, 이미지 모두 비어있는경우")
	void feed_write_test4() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(EMPTY_FEED_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}

	//todo 로그인 추가되면 마저 작성 : 로그인 하지 않은경우 피드 작성 실패

	// @Test
	// @DisplayName("로그인 하지 않은경우 피드 작성 실패")
	// void feed_write_fail() throws Exception {
	// 	// todo 인증되지 않은 상태여야한다
	// 	this.mockMvc.perform(post("/feeds")
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content(this.objectMapper.writeValueAsString(this.rightFeedDto)))
	// 		.andExpect(status().isUnauthorized())
	// 		.andDo(print());
	// }

	@Test
	@DisplayName("피드 수정 성공")
	void feed_update_test() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		//todo 로그인 한 유저

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.file(this.contents)
				.file(this.image1)
				.file(this.image2)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo 로그인 안한경우 수정할때 에러

	//todo 로그인은 되어있지만, 작성자가 아닌 다른사람이 수정하는경우 에러

	//todo 수정하려는 피드가 존재하지 않는경우 에러
}
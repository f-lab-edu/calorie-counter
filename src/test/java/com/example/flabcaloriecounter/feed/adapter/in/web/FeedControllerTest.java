package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.CURSOR_ERROR_MSG;
import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.EMPTY_FEED_MSG;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedRequestDto;
import com.example.flabcaloriecounter.feed.application.service.FeedService;
import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("classpath:tableInit.sql")
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
	private FeedRequestDto contentsFeed;
	private FeedRequestDto contentsFeed2;

	MockMultipartFile image1;
	MockMultipartFile contents;
	MockMultipartFile nullContents;
	MockMultipartFile image2;

	MockMultipartFile mockFeedContent;
	List<MockMultipartFile> list;

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
			"photos",
			"photos",
			"image/jpeg",
			"photos".getBytes()
		);

		this.image2 = new MockMultipartFile(
			"photos",
			"photos2",
			"image/jpeg",
			"photos2".getBytes()
		);

		this.contents = new MockMultipartFile(
			"contents",
			"contents",
			"",
			objectMapper.writeValueAsString(new FeedTestDto("닭가슴살을 먹었다")).getBytes()
		);

		this.nullContents = new MockMultipartFile(
			"contents",
			"contents",
			"",
			objectMapper.writeValueAsString(new FeedTestDto("")).getBytes()
		);

		this.contentsFeed = new FeedRequestDto(
			"닭가슴살을 먹었다",
			null
		);

		this.contentsFeed2 = new FeedRequestDto(
			"닭가슴살을 먹었다2",
			null
		);

		list = List.of(image1, image2);

		this.mockFeedContent = new MockMultipartFile(
			"contents",
			"contents",
			"",
			objectMapper.writeValueAsString("닭가슴살먹음").getBytes()
		);
	}

	//todo 로그인한 유저여야한다
	@Test
	@DisplayName("피드 작성 성공 : 이미지, 내용 둘다 있는경우")
	void feed_write_test() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.file(this.mockFeedContent)
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
				.file(mockFeedContent)
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

	@Test
	@DisplayName("피드 수정 성공: image, contents 둘다 있음")
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

	@Test
	@DisplayName("피드 수정 성공: contents만 있음")
	void feed_update_test2() throws Exception {
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
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 성공: image만 있음")
	void feed_update_test3() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		//todo 로그인 한 유저
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.file(image1)
				.file(image2)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo 로그인한 유저여야한다
	@Test
	@DisplayName("피드 수정 실패 : 내용, 이미지 모두 비어있는경우")
	void feed_update_test4() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		//todo 로그인 한 유저
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/feeds/1");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		this.mockMvc.perform(builder
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(EMPTY_FEED_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}

	//todo 로그인 안한경우 수정할때 에러

	//todo 로그인은 되어있지만, 작성자가 아닌 다른사람이 수정하는경우 에러

	//todo 수정하려는 피드가 존재하지 않는경우 에러

	//todo 로그인
	@Test
	@DisplayName("피드 삭제 성공 : 로그인 한 유저")
	void feed_delete_success() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		this.mockMvc.perform(delete("/feeds/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}

	//todo 로그인

	@Test
	@DisplayName("피드 삭제 실패 : 로그인 하지않은 유저")
	void feed_delete_fail() throws Exception {
		this.mockMvc.perform(delete("/feeds/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	//todo 로그인

	@Test
	@DisplayName("피드 삭제 실패: 피드 작성자가 아님")
	void feed_delete_fail2() throws Exception {
		this.mockMvc.perform(delete("/feeds/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	//todo 로그인

	@Test
	@DisplayName("피드 삭제 실패: 피드가 존재하지않음")
	void feed_delete_fail3() throws Exception {
		this.mockMvc.perform(delete("/feeds/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("피드 조회 성공")
	void feed_read_success() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);
		this.feedService.write(this.contentsFeed, 1);
		this.feedService.write(this.contentsFeed2, 1);

		this.mockMvc.perform(get("/feeds?cursorNo=4&displayPerPage=3")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)))
			.andExpect(jsonPath("$[0].feedId").value(3))
			.andExpect(jsonPath("$[0].contents").value(this.contentsFeed2.contents()))
			.andExpect(jsonPath("$[0].userId").value(this.rightUserForm.userId()))
			.andExpect(jsonPath("$[0].userName").value(this.rightUserForm.userName()))
			.andExpect(jsonPath("$[2].feedId").value(1))
			.andExpect(jsonPath("$[2].contents").value(this.contentsFeed.contents()))
			.andExpect(jsonPath("$[2].userId").value(this.rightUserForm.userId()))
			.andExpect(jsonPath("$[2].userName").value(this.rightUserForm.userName()))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 조회 실패: cursor가 음수")
	void feed_read_fail() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);
		this.feedService.write(this.contentsFeed, 1);
		this.feedService.write(this.contentsFeed2, 1);

		this.mockMvc.perform(get("/feeds?cursorNo=-1&displayPerPage=3")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("message").value(CURSOR_ERROR_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}
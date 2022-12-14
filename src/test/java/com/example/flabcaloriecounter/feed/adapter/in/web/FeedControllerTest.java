package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.EMPTY_FEED_MSG;
import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.NOT_EXIST_FEED_MSG;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.example.flabcaloriecounter.feed.application.port.out.FeedPort;
import com.example.flabcaloriecounter.feed.application.service.FeedService;
import com.example.flabcaloriecounter.feed.domain.LikeStatus;
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
	private FeedPort feedPort;

	@Autowired
	private ObjectMapper objectMapper;

	SignUpForm rightUserForm;
	private FeedRequestDto contentsFeed;
	private FeedRequestDto contentsAndPhotoFeed;

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
			"???????????????",
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
			objectMapper.writeValueAsString(new FeedTestDto("??????????????? ?????????")).getBytes()
		);

		this.nullContents = new MockMultipartFile(
			"contents",
			"contents",
			"",
			objectMapper.writeValueAsString(new FeedTestDto("")).getBytes()
		);

		this.contentsFeed = new FeedRequestDto(
			"??????????????? ?????????",
			null
		);

		this.contentsAndPhotoFeed = new FeedRequestDto(
			"??????????????? ?????????2",
			List.of(image1, image2)
		);

		list = List.of(image1, image2);

		this.mockFeedContent = new MockMultipartFile(
			"contents",
			"contents",
			"",
			objectMapper.writeValueAsString("??????????????????").getBytes()
		);
	}

	//todo ???????????? ??????????????????
	@Test
	@DisplayName("?????? ?????? ?????? : ?????????, ?????? ?????? ????????????")
	void feed_write_test() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.file(this.mockFeedContent)
				.file(this.image1)
				.file(this.image2)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo ???????????? ??????????????????
	@Test
	@DisplayName("?????? ?????? ?????? : ???????????? ????????????")
	void feed_write_test2() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.file(this.image1)
				.file(this.image2)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo ???????????? ??????????????????
	@Test
	@DisplayName("?????? ?????? ?????? : ????????? ????????????")
	void feed_write_test3() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.file(mockFeedContent)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo ???????????? ??????????????????
	@Test
	@DisplayName("?????? ?????? ?????? : ??????, ????????? ?????? ??????????????????")
	void feed_write_test4() throws Exception {
		this.mockMvc.perform(multipart("/feeds")
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(EMPTY_FEED_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}

	@Test
	@DisplayName("?????? ?????? ??????: image, contents ?????? ??????")
	void feed_update_test() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		//todo ????????? ??? ??????
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
	@DisplayName("?????? ?????? ??????: contents??? ??????")
	void feed_update_test2() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		//todo ????????? ??? ??????
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
	@DisplayName("?????? ?????? ??????: image??? ??????")
	void feed_update_test3() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		//todo ????????? ??? ??????
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

	//todo ???????????? ??????????????????
	@Test
	@DisplayName("?????? ?????? ?????? : ??????, ????????? ?????? ??????????????????")
	void feed_update_test4() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		//todo ????????? ??? ??????
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

	//todo ????????? ???????????? ???????????? ??????

	//todo ???????????? ???????????????, ???????????? ?????? ??????????????? ?????????????????? ??????

	//todo ??????????????? ????????? ???????????? ???????????? ??????

	//todo ?????????
	@Test
	@DisplayName("?????? ?????? ?????? : ????????? ??? ??????")
	void feed_delete_success() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);

		this.mockMvc.perform(delete("/feeds/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}

	//todo ?????????

	// @Test
	// @DisplayName("?????? ?????? ?????? : ????????? ???????????? ??????")
	// void feed_delete_fail() throws Exception {
	// 	this.mockMvc.perform(delete("/feeds/1")
	// 			.contentType(MediaType.APPLICATION_JSON))
	// 		.andDo(print())
	// 		.andExpect(status().isUnauthorized());
	// }

	//todo ?????????

	// @Test
	// @DisplayName("?????? ?????? ??????: ?????? ???????????? ??????")
	// void feed_delete_fail2() throws Exception {
	// 	this.mockMvc.perform(delete("/feeds/1")
	// 			.contentType(MediaType.APPLICATION_JSON))
	// 		.andDo(print())
	// 		.andExpect(status().isUnauthorized());
	// }

	//todo ?????????

	@Test
	@DisplayName("?????? ?????? ??????: ????????? ??????????????????")
	void feed_delete_fail3() throws Exception {
		this.mockMvc.perform(delete("/feeds/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("?????? ?????? ??????: ???????????????,???????????????, ??????, ?????????")
	void feed_read_success() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsAndPhotoFeed, 1);
		this.feedService.write(this.contentsFeed, 1); // 1
		this.feedService.write(this.contentsFeed, 1); // 2
		this.feedService.like(1, this.rightUserForm.userId());

		this.mockMvc.perform(get("/feeds?cursorNo=4&displayPerPage=3")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)))
			.andExpect(jsonPath("$[2].id").value(1))
			.andExpect(jsonPath("$[2].contents").value(this.contentsAndPhotoFeed.contents()))
			.andExpect(jsonPath("$[2].userId").value(1))
			.andExpect(jsonPath("$[2].photos.length()", is(2)))
			.andExpect(jsonPath("$[2].likeCount").value(1))
			.andExpect(jsonPath("$[2].likeStatus").value("ACTIVATE"))
			.andExpect(jsonPath("$[0].id").value(3))
			.andExpect(jsonPath("$[0].contents").value(this.contentsFeed.contents()))
			.andExpect(jsonPath("$[0].userId").value(1))
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("?????? ?????? ??????: cursor??? 0?????? ????????? maxId?????? ??????.")
	void feed_read_fail() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);
		this.feedService.write(this.contentsAndPhotoFeed, 1);
		this.feedService.write(this.contentsAndPhotoFeed, 1);

		this.mockMvc.perform(get("/feeds?cursorNo=-1&displayPerPage=3")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)))
			.andExpect(jsonPath("$[0].id").value(3))
			.andExpect(jsonPath("$[0].contents").value(this.contentsAndPhotoFeed.contents()))
			.andExpect(jsonPath("$[0].userId").value(1))

			.andExpect(jsonPath("$[2].id").value(1))
			.andExpect(jsonPath("$[2].contents").value(this.contentsFeed.contents()))
			.andExpect(jsonPath("$[2].userId").value(1))
			.andDo(print())
			.andExpect(status().isOk());
	}

	//todo ?????????
	@Test
	@DisplayName("?????? ????????? ?????? ??????")
	void feed_like_success2() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);
		this.feedService.write(this.contentsFeed, 1);
		this.feedService.write(this.contentsFeed, 1);
		this.feedService.write(this.contentsAndPhotoFeed, 1);
		this.feedPort.like(1, 1, LikeStatus.ACTIVATE);

		this.mockMvc.perform(post("/feeds/1/like")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}


	//todo ?????????
	@Test
	@DisplayName("?????? ????????? ??????: ???????????? ?????? ?????????")
	void feed_like_fail2() throws Exception {
		this.signUpUseCase.signUp(this.rightUserForm);

		this.mockMvc.perform(post("/feeds/1/like")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("message").value(NOT_EXIST_FEED_MSG))
			.andExpect(jsonPath("statusCode").value("NOT_FOUND"))
			.andDo(print())
			.andExpect(status().isNotFound());
	}
}
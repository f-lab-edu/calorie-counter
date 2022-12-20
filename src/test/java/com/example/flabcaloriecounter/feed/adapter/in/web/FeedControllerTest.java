package com.example.flabcaloriecounter.feed.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.example.flabcaloriecounter.feed.application.port.in.response.FeedDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class FeedControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	FeedDto rightFeedDto;
	FeedDto wrongFeedDto;

	@BeforeEach
	void setup() {
		this.rightFeedDto = new FeedDto(
			"닭가슴살을 먹었다"
		);

		this.wrongFeedDto = new FeedDto(
			""
		);

	}

	@Test
	@DisplayName("피드 작성 성공 : 이미지, 내용 둘다 있는경우")
	void feed_photo_test() throws Exception {
		MockMultipartFile image1 = new MockMultipartFile(
			"photos",
			"photos",
			"image/jpeg",
			"photos".getBytes()
		);
		MockMultipartFile image2 = new MockMultipartFile(
			"photos",
			"photos2",
			"image/jpeg",
			"photos2".getBytes()
		);

		this.mockMvc.perform(multipart("/feeds")
				.file(image1)
				.file(image2)
				.param("content", this.rightFeedDto.contents())
			)
			.andDo(print())
			.andExpect(status().isCreated());
	}

	//todo 피드 작성 성공 : 이미지나 내용 둘중 하나만 있는경우

	//todo 피드 작성 실패 : 내용, 이미지 모두 비어있음

	// @Test
	// @DisplayName("피드 작성 실패 : 내용, 이미지 모두 비어있음")
	// void feed_write_fail_emptyContent() throws Exception {
	// 	this.mockMvc.perform(post("/feeds")
	// 			.content(this.objectMapper.writeValueAsString(this.wrongFeedDto)))
	// 		.andExpect(status().isBadRequest())
	// 		.andExpect(jsonPath("message").value(ARGUMENT_NOT_VALID_MSG))
	// 		.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
	// 		.andDo(print());
	// }

	//todo 로그인 추가되면 마저 작성

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
}
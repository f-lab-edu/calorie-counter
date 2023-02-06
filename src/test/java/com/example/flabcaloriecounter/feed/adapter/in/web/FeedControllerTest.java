package com.example.flabcaloriecounter.feed.adapter.in.web;

import static com.example.flabcaloriecounter.util.CustomResponse.ERROR;
import static com.example.flabcaloriecounter.util.CustomResponse.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.feed.application.port.in.dto.CommentRequestDto;
import com.example.flabcaloriecounter.feed.application.port.in.dto.FeedDto;
import com.example.flabcaloriecounter.feed.application.service.FeedService;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.util.StatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FeedControllerTest {
	public static final String FEED_WRITE_URL = "/feeds";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String AUTHORIZATION_BEARER = "Bearer ";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private FeedService feedService;

	@Autowired
	private ObjectMapper objectMapper;

	private ResponseIssuedToken responseIssuedToken;

	CommentRequestDto commentRequestDto = new CommentRequestDto("댓글1");

	private final SignUpForm wrongSignUpForm = new SignUpForm("wrongUser", "유저1의이름", "asdf1234", "dudwls0505@nate.com");
	private final SignUpForm alreadySignUpForm = new SignUpForm("mockUser", "이영진", "asdf1234", "dudwls0505@naver.com");
	private final LoginForm alreadyLoginForm = new LoginForm("mockUser", "asdf1234");

	MockMultipartFile image1 = new MockMultipartFile("photos", "photos", "image/jpeg", "photos".getBytes());
	MockMultipartFile image2 = new MockMultipartFile("photos", "photos2", "image/jpeg", "photos2".getBytes());

	FeedDto notWriteFeed = new FeedDto("게시글내용1", List.of(this.image1, this.image2),
		alreadySignUpForm.getId());
	FeedDto feedWithContents = new FeedDto("게시글내용1", alreadySignUpForm.getId());
	FeedDto feedWithPhoto = new FeedDto(List.of(this.image1, this.image2), alreadySignUpForm.getId());

	record FeedTestDto(String contents) {
	}

	private MockMultipartFile contents;

	@BeforeEach
	void setup() throws JsonProcessingException {
		this.responseIssuedToken = this.userService.login(alreadyLoginForm);
		feedService.write(feedWithContents);
		feedService.write(feedWithPhoto);
		feedService.write(feedWithPhoto);

		contents = new MockMultipartFile("contents", "contents", "",
			objectMapper.writeValueAsString(new FeedTestDto("닭가슴살먹었다")).getBytes());
	}

	@Test
	@DisplayName("피드 수정 성공: 수정내용에 image, contents 둘다 있음")
	void feed_update_test() throws Exception {
		MockMultipartHttpServletRequestBuilder builder = multipart("/feeds/" + feedWithContents.getId());
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		mockMvc.perform(builder
				.file(contents)
				.file(image1)
				.file(image2)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 성공: 수정내용에 contents만 있음")
	void feed_update_test2() throws Exception {
		MockMultipartHttpServletRequestBuilder builder = multipart("/feeds/" + feedWithContents.getId());
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		mockMvc.perform(builder
				.file(contents)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 성공: 수정내용에 image만 있음")
	void feed_update_test3() throws Exception {
		MockMultipartHttpServletRequestBuilder builder = multipart("/feeds/" + feedWithContents.getId());
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		mockMvc.perform(builder
				.file(image1)
				.file(image2)
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("피드 수정 실패 : 수정내용에 내용, 이미지 모두 비어있는경우")
	void feed_update_test4() throws Exception {
		MockMultipartHttpServletRequestBuilder builder = multipart("/feeds/" + feedWithContents.getId());
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		mockMvc.perform(builder
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MULTIPART_FORM_DATA))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_FEED.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("피드 삭제 성공 : 로그인 한 유저")
	void feed_delete_success() throws Exception {
		mockMvc.perform(delete("/feeds/" + feedWithContents.getId())
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 조회 성공: cursor가 0보다 작으면 maxId값을 준다.")
	void feed_read_fail() throws Exception {
		mockMvc.perform(get("/feeds?cursorNo=-1&displayPerPage=3")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.info.length()", is(3)))
			.andExpect(jsonPath("$.info[0].contents").value(feedWithPhoto.getContents()))
			.andExpect(jsonPath("$.info[1].contents").value(feedWithPhoto.getContents()))
			.andExpect(jsonPath("$.info[2].contents").value(feedWithContents.getContents()))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("피드 조회 성공: 좋아요개수,좋아요상태, 사진, 글내용,댓글, 대댓글")
	void feed_read_success() throws Exception {
		feedService.like(feedWithContents.getId(), alreadySignUpForm.getId());
		feedService.comment(feedWithContents.getId(), alreadySignUpForm.getId(), commentRequestDto);
		feedService.comment(feedWithContents.getId(), alreadySignUpForm.getId(), commentRequestDto);
		feedService.comment(feedWithContents.getId(), alreadySignUpForm.getId(), commentRequestDto);

		mockMvc.perform(get("/feeds?cursorNo=50&displayPerPage=3&commentPageNum=1&commentPerPage=3")
				.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("result").value(SUCCESS))
			.andExpect(jsonPath("$.info.length()", is(3)))

			.andExpect(jsonPath("$.info[0].contents").value(feedWithPhoto.getContents()))
			.andExpect(jsonPath("$.info[1].contents").value(feedWithPhoto.getContents()))
			.andExpect(jsonPath("$.info[2].contents").value(feedWithContents.getContents()))

			.andExpect(jsonPath("$.info[0].photos.length()", is(2)))
			.andExpect(jsonPath("$.info[1].photos.length()", is(2)))
			.andExpect(jsonPath("$.info[2].photos.length()", is(0)))

			.andExpect(jsonPath("$.info[0].likeCount").value(0))
			.andExpect(jsonPath("$.info[1].likeCount").value(0))
			.andExpect(jsonPath("$.info[2].likeCount").value(1))

			.andExpect(jsonPath("$.info[0].likeStatus").value(IsNull.nullValue()))
			.andExpect(jsonPath("$.info[1].likeStatus").value(IsNull.nullValue()))
			.andExpect(jsonPath("$.info[2].likeStatus").value("ACTIVATE"))

			.andExpect(jsonPath("$.info[0].comments.length()", is(0)))
			.andExpect(jsonPath("$.info[1].comments.length()", is(0)))
			.andExpect(jsonPath("$.info[2].comments.length()", is(3)))

			.andDo(print())
			.andExpect(status().isOk());
	}

	@Nested
	@DisplayName("작성된 피드가 없는경우")
	class FeedNotExistBlock {

		@BeforeEach
		void setup() throws JsonProcessingException {
			contents = new MockMultipartFile("contents", "contents", "",
				objectMapper.writeValueAsString(new FeedTestDto("닭가슴살먹었다")).getBytes());
		}

		@Nested
		@DisplayName("피드 작성")
		class FeedWriteBlock {

			@Test
			@DisplayName("피드 작성 성공 : 이미지, 내용 둘다 있는경우")
			void feed_write_test() throws Exception {
				mockMvc.perform(multipart(FEED_WRITE_URL)
						.file(contents)
						.file(image1)
						.file(image2)
						.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
						.contentType(MULTIPART_FORM_DATA))
					.andDo(print())
					.andExpect(status().isCreated());
			}

			@Test
			@DisplayName("피드 작성 성공 : 이미지만 있는경우")
			void feed_write_test2() throws Exception {
				mockMvc.perform(multipart(FEED_WRITE_URL)
						.file(image1)
						.file(image2)
						.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
						.contentType(MULTIPART_FORM_DATA))
					.andDo(print())
					.andExpect(status().isCreated());
			}

			@Test
			@DisplayName("피드 작성 성공 : 내용만 있는경우")
			void feed_write_test3() throws Exception {
				mockMvc.perform(multipart(FEED_WRITE_URL)
						.file(contents)
						.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
						.contentType(MULTIPART_FORM_DATA))
					.andDo(print())
					.andExpect(status().isCreated());
			}

			@Test
			@DisplayName("피드 작성 실패 : 내용, 이미지 모두 비어있는경우")
			void feed_write_test4() throws Exception {
				mockMvc.perform(multipart(FEED_WRITE_URL)
						.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
						.contentType(MULTIPART_FORM_DATA))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("result").value(ERROR))
					.andExpect(jsonPath("errorMessage").value(StatusEnum.EMPTY_FEED.getMessage()))
					.andDo(print());
			}
		}

		@Test
		@DisplayName("피드 수정 실패")
		void feed_update_test3() throws Exception {
			MockMultipartHttpServletRequestBuilder builder = multipart("/feeds/" + notWriteFeed.getId());
			builder.with(request -> {
				request.setMethod("PUT");
				return request;
			});

			mockMvc.perform(builder
					.file(image1)
					.file(image2)
					.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
					.contentType(MULTIPART_FORM_DATA))
				.andDo(print())
				.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("피드 삭제 실패")
		void feed_delete_fail3() throws Exception {
			mockMvc.perform(delete("/feeds/" + notWriteFeed.getId())
					.header(AUTHORIZATION_HEADER, AUTHORIZATION_BEARER + responseIssuedToken.accessToken())
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
		}
	}
}
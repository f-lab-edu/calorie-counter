package com.example.flabcaloriecounter.user.adapter.in.web;

import static com.example.flabcaloriecounter.util.CustomResponse.ERROR;
import static com.example.flabcaloriecounter.util.CustomResponse.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.util.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	private final String LOGIN_URL = "/login";
	private final String USERS_URL = "/users";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	private final SignUpForm rightSignUpForm = new SignUpForm(
		"joinUserId",
		"회원가입용유저",
		"asdf1234",
		"dudwls0505@nate.com"
	);

	private final SignUpForm alreadySignUpForm = new SignUpForm(
		"mockUser",
		"이영진",
		"asdf1234",
		"dudwls0505@naver.com"
	);

	private final SignUpForm wrongInputForm = new SignUpForm(
		"wrongUserId",
		"잘못된유저",
		"1",
		"2"
	);

	@Test
	@DisplayName("회원가입 성공")
	void signUp_success() throws Exception {
		mockMvc.perform(post(USERS_URL)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rightSignUpForm)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("result").value(SUCCESS))
			.andExpect(jsonPath("$.info.userId").value(rightSignUpForm.getUserId()))
			.andExpect(jsonPath("$.info.userName").value(rightSignUpForm.getUserName()))
			.andExpect(jsonPath("$.info.email").value(rightSignUpForm.getEmail()))
			.andDo(print());
	}

	@Test
	@DisplayName("회원가입 실패 : 입력하지 않은 필드값 존재")
	void signUp_wrong_input_fail() throws Exception {
		mockMvc.perform(post(USERS_URL)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongInputForm)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.INVALID_FORM_INPUT.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("회원가입 실패 : 중복 회원가입 시도시 예외를 던진다.")
	void signUp_AlreadyExistUserIdException_fail() throws Exception {
		mockMvc.perform(post(USERS_URL)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(alreadySignUpForm)))
			.andDo(print())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.DUPLICATED_ID.getMessage()))
			.andExpect(status().isConflict());
	}
	// }

	@Nested
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("로그인")
	class loginBlock {

		private final LoginForm rightLoginForm = new LoginForm(
			rightSignUpForm.getUserId(),
			rightSignUpForm.getUserPassword()
		);
		private final LoginForm wrongLoginForm = new LoginForm(
			"join_wrongUserId",
			null
		);
		private final LoginForm wrongPasswordLoginForm = new LoginForm(
			rightSignUpForm.getUserId(),
			"wrongPassword234"
		);
		private final LoginForm notExistIdLoginForm = new LoginForm(
			"notExistId",
			rightSignUpForm.getUserPassword()
		);

		@BeforeAll
		void setup() {
			userService.signUp(rightSignUpForm);
		}

		@Test
		@DisplayName("로그인 성공")
		void login_success_test() throws Exception {
			mockMvc.perform(post(LOGIN_URL)
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(this.rightLoginForm)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("result").value(SUCCESS))
				.andExpect(jsonPath("$.info.length()", is(3)))
				.andDo(print());
		}

		@Test
		@DisplayName("로그인 실패: 입력하지 않은 필드값 존재")
		void login_fail_test() throws Exception {
			mockMvc.perform(post(LOGIN_URL)
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(this.wrongLoginForm)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("result").value(ERROR))
				.andExpect(jsonPath("errorMessage").value(StatusEnum.INVALID_FORM_INPUT.getMessage()))
				.andDo(print());
		}

		@Test
		@DisplayName("로그인 실패: 존재하지 않는 ID")
		void login_fail_test2() throws Exception {
			mockMvc.perform(post(LOGIN_URL)
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(this.notExistIdLoginForm)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("result").value(ERROR))
				.andExpect(jsonPath("errorMessage").value(StatusEnum.USER_NOT_FOUND.getMessage()))
				.andDo(print());
		}

		@Test
		@DisplayName("로그인 실패: 비밀번호 일치하지 않음")
		void login_fail_test3() throws Exception {
			mockMvc.perform(post(LOGIN_URL)
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(this.wrongPasswordLoginForm)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("result").value(ERROR))
				.andExpect(jsonPath("errorMessage").value(StatusEnum.PASSWORD_NOT_MATCH.getMessage()))
				.andDo(print());
		}
	}
}

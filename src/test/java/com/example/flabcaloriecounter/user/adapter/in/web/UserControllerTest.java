package com.example.flabcaloriecounter.user.adapter.in.web;

import static com.example.flabcaloriecounter.user.source.TestUserSource.rightLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightSignUpForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongPasswordLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongSignUpForm;
import static com.example.flabcaloriecounter.util.CustomResponse.ERROR;
import static com.example.flabcaloriecounter.util.CustomResponse.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.user.application.port.in.UserUseCase;
import com.example.flabcaloriecounter.user.application.service.UserCacheService;
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
	private UserUseCase userUseCase;

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserCacheService userCacheService;

	@BeforeEach
	void setup() {
		this.userCacheService.deleteAll();
	}

	@Test
	@DisplayName("회원가입 성공")
	void signUp_success() throws Exception {
		this.mockMvc.perform(post(USERS_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rightSignUpForm)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("result").value(SUCCESS))
			.andExpect(jsonPath("$.info.userId").value(rightSignUpForm.userId()))
			.andExpect(jsonPath("$.info.userName").value(rightSignUpForm.userName()))
			.andExpect(jsonPath("$.info.userPassword").value(rightSignUpForm.userPassword()))
			.andExpect(jsonPath("$.info.email").value(rightSignUpForm.email()))
			.andExpect(jsonPath("$.info.weight").value(rightSignUpForm.weight()))
			.andExpect(jsonPath("$.info.userType").value("ORDINARY"))
			.andDo(print());
	}

	@Test
	@DisplayName("잘못된 Form 입력값으로 회원가입 실패")
	void signUp_wrong_input_fail() throws Exception {
		this.mockMvc.perform(post(USERS_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(wrongSignUpForm)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.INVALID_FORM_INPUT.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("중복 회원가입 시도시 예외를 던진다.")
	void signUp_AlreadyExistUserIdException_fail() throws Exception {
		//given
		this.userService.signUp(rightSignUpForm);

		this.mockMvc.perform(post(USERS_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(rightSignUpForm)))
			.andDo(print())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.DUPLICATED_ID.getMessage()))
			.andExpect(status().isConflict());
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success_test() throws Exception {
		//given
		this.userService.signUp(rightSignUpForm);

		this.mockMvc.perform(post(LOGIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rightLoginForm)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("result").value(SUCCESS))
			.andExpect(jsonPath("$.info.length()", is(3)))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패: 입력하지 않은 필드값 존재")
	void login_fail_test() throws Exception {
		this.mockMvc.perform(post(LOGIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongLoginForm)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.INVALID_FORM_INPUT.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패: 존재하지 않는 ID")
	void login_fail_test2() throws Exception {
		this.mockMvc.perform(post(LOGIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rightLoginForm)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.USER_NOT_FOUND.getMessage()))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패: 비밀번호 일치하지 않음")
	void login_fail_test3() throws Exception {
		//given
		this.userService.signUp(rightSignUpForm);

		this.mockMvc.perform(post(LOGIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongPasswordLoginForm)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("result").value(ERROR))
			.andExpect(jsonPath("errorMessage").value(StatusEnum.PASSWORD_NOT_MATCH.getMessage()))
			.andDo(print());
	}
}

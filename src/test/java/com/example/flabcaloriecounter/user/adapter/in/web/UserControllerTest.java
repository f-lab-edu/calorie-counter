package com.example.flabcaloriecounter.user.adapter.in.web;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.ARGUMENT_NOT_VALID_MSG;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightSignUpForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongPasswordLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongSignUpForm;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	private static final String PASSWORD_NOT_MATCH_MSG = "비밀번호가 일치하지 않습니다.";
	private static final String NOT_EXIST_USER_MSG = "존재하지 않는 유저입니다.";
	private static final String HAS_DUPLICATED_ID_MSG = "이미 존재하는 ID 입니다";

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
		this.mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rightSignUpForm)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("userId").value(rightSignUpForm.userId()))
			.andExpect(jsonPath("userName").value(rightSignUpForm.userName()))
			.andExpect(jsonPath("userPassword").value(rightSignUpForm.userPassword()))
			.andExpect(jsonPath("email").value(rightSignUpForm.email()))
			.andExpect(jsonPath("weight").value(rightSignUpForm.weight()))
			.andExpect(jsonPath("userType").value("ORDINARY"))
			.andDo(print());
	}

	@Test
	@DisplayName("잘못된 Form 입력값으로 회원가입 실패")
	void signUp_wrong_input_fail() throws Exception {
		this.mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(wrongSignUpForm)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(ARGUMENT_NOT_VALID_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}

	@Test
	@DisplayName("중복 회원가입 시도시 AlreadyExistUserIdException 예외를 던진다.")
	void signUp_AlreadyExistUserIdException_fail() throws Exception {
		//when
		this.userService.signUp(rightSignUpForm);

		this.mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(rightSignUpForm)))
			.andDo(print())
			.andExpect(jsonPath("message").value(HAS_DUPLICATED_ID_MSG))
			.andExpect(jsonPath("statusCode").value("CONFLICT"))
			.andExpect(status().isConflict());
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success_test() throws Exception {
		//when
		this.userService.signUp(rightSignUpForm);

		this.mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rightLoginForm)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", is(2)))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패: 입력하지 않은 필드값 존재")
	void login_fail_test() throws Exception {
		this.mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongLoginForm)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(ARGUMENT_NOT_VALID_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패: 존재하지 않는 ID")
	void login_fail_test2() throws Exception {
		this.mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rightLoginForm)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("message").value(NOT_EXIST_USER_MSG))
			.andExpect(jsonPath("statusCode").value("NOT_FOUND"))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 실패: 비밀번호 일치하지 않음")
	void login_fail_test3() throws Exception {
		//when
		this.userService.signUp(rightSignUpForm);

		this.mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongPasswordLoginForm)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message").value(PASSWORD_NOT_MATCH_MSG))
			.andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
			.andDo(print());
	}
}

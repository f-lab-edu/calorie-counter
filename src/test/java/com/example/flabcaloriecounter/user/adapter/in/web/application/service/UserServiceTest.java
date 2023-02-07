package com.example.flabcaloriecounter.user.adapter.in.web.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.application.service.UserCacheService;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.StatusEnum;

@SpringBootTest
@Transactional
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private SignUpPort signUpPort;

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
	@DisplayName("회원가입 실패 : 중복된 아이디")
	void signUp_existEmail_fail(final SignUpForm signUpForm) {
		//given
		assertDoesNotThrow(() -> userService.signUp(signUpForm));

		CustomException customException = assertThrows(CustomException.class,
			() -> userService.signUp(signUpForm));
		assertThat(StatusEnum.DUPLICATED_ID).isEqualTo(customException.getStatusEnum());
	}

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
	@DisplayName("회원가입 성공")
	void signUp_success(final SignUpForm signUpForm) {
		//given
		assertDoesNotThrow(() -> userService.signUp(signUpForm));
		User user = signUpPort.findByUserId(signUpForm.getUserId()).orElseThrow();

		assertAll(
			() -> assertThat(user).isNotNull(),
			() -> assertThat(user.userId()).isEqualTo(signUpForm.getUserId()),
			() -> assertThat(user.name()).isEqualTo(signUpForm.getUserName()),
			() -> assertThat(user.email()).isEqualTo(signUpForm.getEmail())
		);
	}

	@Nested
	@DisplayName("로그인")
	class LoginBlock {
		private final SignUpForm alreadySignUpForm = new SignUpForm(
			"mockUser",
			"이영진",
			"asdf1234",
			"dudwls0505@naver.com"
		);

		private final LoginForm alreadyLoginForm = new LoginForm(
			"mockUser",
			"asdf1234"
		);

		private final LoginForm wrongLoginForm = new LoginForm(
			"notExistTestId",
			"asdf1234"
		);
		private final LoginForm wrongPasswordLoginForm = new LoginForm(
			"mockUser",
			"WRONG_PASSWORD"
		);

		@BeforeEach
		void setup() {
			userCacheService.deleteAll();
		}

		@Test
		@DisplayName("로그인 성공 : redis에 유저를 캐시한다.")
		void login_success3() {
			//given
			assertThat(userCacheService.getUser(this.alreadySignUpForm.getUserId())).isEmpty();

			ResponseIssuedToken login = userService.login(this.alreadyLoginForm);

			//then
			assertThat(login.accessToken()).isNotNull();
			assertThat(userCacheService.getUser(this.alreadyLoginForm.userId())).isNotNull();
		}

		@Test
		@DisplayName("로그인 실패: 존재하지 않는 ID")
		void login_fail() {
			CustomException customException = assertThrows(CustomException.class,
				() -> userService.login(this.wrongLoginForm));
			assertThat(StatusEnum.USER_NOT_FOUND).isEqualTo(customException.getStatusEnum());
		}

		@Test
		@DisplayName("로그인 실패: 일치하지 않는 비밀번호")
		void login_fail3() {
			//given
			CustomException customException = assertThrows(CustomException.class,
				() -> userService.login(this.wrongPasswordLoginForm));
			assertThat(StatusEnum.PASSWORD_NOT_MATCH).isEqualTo(customException.getStatusEnum());
		}

		//todo

		@Test
		@DisplayName("캐싱된 유저는 DB유저와 일치한다.")
		void user_cache_success() {
			//given
			userService.login(this.alreadyLoginForm);

			assertThat(userCacheService.getUser(this.alreadySignUpForm.getUserId())).isNotNull();
			assertThat(userService.findByUserId(this.alreadySignUpForm.getUserId()).orElseThrow()).isEqualTo(
				userCacheService.getUser(this.alreadySignUpForm.getUserId()).orElseThrow());
		}
	}
}

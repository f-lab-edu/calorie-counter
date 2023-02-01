package com.example.flabcaloriecounter.user.application.service;

import static com.example.flabcaloriecounter.user.source.TestUserSource.rightLoginForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.rightSignUpForm;
import static com.example.flabcaloriecounter.user.source.TestUserSource.wrongPasswordLoginForm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.StatusEnum;

@SpringBootTest
@Transactional
@Sql("classpath:tableInit.sql")
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private SignUpPort signUpPort;

	@BeforeEach
	void setup() {
		this.userCacheService.deleteAll();
	}

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
	@DisplayName("중복된 아이디가 있으면 예외를 던진다")
	void signUp_existEmail_fail(final SignUpForm signUpForm) {
		//given
		this.userService.signUp(signUpForm);

		CustomException customException = assertThrows(CustomException.class,
			() -> this.userService.signUp(signUpForm));
		assertThat(StatusEnum.DUPLICATED_ID).isEqualTo(customException.getStatusEnum());
	}

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
	@DisplayName("중복된 아이디가 없으면 정상처리한다")
	void signUp_success(final SignUpForm signUpForm) {
		assertDoesNotThrow(() -> this.userService.signUp(signUpForm));
		User user = this.signUpPort.findByUserId(signUpForm.userId()).orElseThrow();

		assertAll(
			() -> assertThat(user).isNotNull(),
			() -> assertThat(user.userId()).isEqualTo(signUpForm.userId()),
			() -> assertThat(user.name()).isEqualTo(signUpForm.userName()),
			() -> assertThat(user.email()).isEqualTo(signUpForm.email()),
			() -> assertThat(user.userType()).isEqualTo(signUpForm.userType()),
			() -> assertThat(user.judgeStatus()).isEqualTo(signUpForm.judgeStatus())
		);
	}

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#정보제공자_3가지")
	@DisplayName("정보제공자가 성공적으로 가입될 수 있다")
	void 정보제공자_가입_성공(final SignUpForm providerSignUpForm) {
		//given
		this.userService.signUp(providerSignUpForm);

		// then
		final User checkedProvider = this.signUpPort.findByUserId(providerSignUpForm.userId()).orElseThrow();
		assertAll(
			() -> assertThat(checkedProvider).isNotNull(),
			() -> assertThat(checkedProvider.userId()).isEqualTo(providerSignUpForm.userId()),
			() -> assertThat(checkedProvider.name()).isEqualTo(providerSignUpForm.userName()),
			() -> assertThat(checkedProvider.email()).isEqualTo(providerSignUpForm.email()),
			() -> assertThat(checkedProvider.userType()).isEqualTo(providerSignUpForm.userType()),
			() -> assertThat(checkedProvider.judgeStatus()).isEqualTo(JudgeStatus.PENDING)
		);
	}

	@Test
	@DisplayName("로그인 성공 : refreshToken이 없는경우, 토큰2개를 발급한후 Redis에 refreshToken을 저장한다.")
	void login_success() {
		//given
		this.userService.signUp(rightSignUpForm);

		//when
		ResponseIssuedToken login = this.userService.login(rightLoginForm);

		//then
		assertAll(
			() -> assertDoesNotThrow(() -> login),
			() -> assertThat(login.accessToken()).isNotNull()
		);
	}

	@Test
	@DisplayName("로그인 성공 : refreshToken이 이미 발급된경우, accessToken만 발급한다.")
	void login_success2() {
		//given
		this.userService.signUp(rightSignUpForm);

		//when
		ResponseIssuedToken login = this.userService.login(rightLoginForm);

		//then
		assertAll(
			() -> assertDoesNotThrow(() -> login),
			() -> assertThat(login.accessToken()).isNotNull()
		);
	}

	@Test
	@DisplayName("로그인 성공 : redis에 유저를 캐시한다.")
	void login_success3() {
		//given
		assertThat(this.userCacheService.getUser(rightLoginForm.userId())).isEmpty();
		this.userService.signUp(rightSignUpForm);

		//when
		ResponseIssuedToken login = this.userService.login(rightLoginForm);

		//then
		assertAll(
			() -> assertDoesNotThrow(() -> login),
			() -> assertThat(this.userCacheService.getUser(rightLoginForm.userId()).orElseThrow()).isNotNull()
		);
	}

	@Test
	@DisplayName("로그인 실패: 존재하지 않는 ID")
	void login_fail() {
		CustomException customException = assertThrows(CustomException.class,
			() -> this.userService.login(rightLoginForm));
		assertThat(StatusEnum.USER_NOT_FOUND).isEqualTo(customException.getStatusEnum());
	}

	@Test
	@DisplayName("로그인 실패: 일치하지 않는 비밀번호")
	void login_fail3() {
		//given
		this.userService.signUp(rightSignUpForm);

		CustomException customException = assertThrows(CustomException.class,
			() -> this.userService.login(wrongPasswordLoginForm));
		assertThat(StatusEnum.PASSWORD_NOT_MATCH).isEqualTo(customException.getStatusEnum());

	}

	@Test
	@DisplayName("캐싱된 유저는 DB유저와 일치한다.")
	void user_cache_success() {
		//given
		this.userService.signUp(rightSignUpForm);
		this.userService.login(rightLoginForm);

		assertThat(this.userCacheService.getUser(rightLoginForm.userId())).isNotNull();
		assertThat(this.userService.findByUserId(rightLoginForm.userId()).orElseThrow()).isEqualTo(
			this.userCacheService.getUser(rightLoginForm.userId()).orElseThrow());
	}
}

package com.example.flabcaloriecounter.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserMapper;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserMybatisRepository;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserPersistenceAdapter;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserRepository;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.User;

@SpringBootTest
@Transactional
class SignUpServiceTest {

	@Autowired
	private SignUpService signUpService;

	@Autowired
	private SignUpPort signUpPort;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	UserPersistenceAdapter userPersistenceAdapter;

	SignUpService realSignUpService;

	@BeforeEach
	void setup() {
		userRepository = new UserMybatisRepository(userMapper);
		userPersistenceAdapter = new UserPersistenceAdapter(userRepository);
		realSignUpService = new SignUpService(userPersistenceAdapter);
	}

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#???????????????_1??????_???????????????_1??????")
	@DisplayName("????????? ???????????? ????????? ????????? ?????????")
	void signUp_existEmail_fail(final SignUpForm signUpForm) {
		this.signUpService.signUp(signUpForm);

		assertThatThrownBy(() -> this.signUpService.signUp(signUpForm))
			.isInstanceOf(HasDuplicatedIdException.class);
	}

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#???????????????_1??????_???????????????_1??????")
	@DisplayName("????????? ???????????? ????????? ??????????????????")
	void signUp_success(final SignUpForm signUpForm) {

		assertDoesNotThrow(() -> this.signUpService.signUp(signUpForm));
	}

	@ParameterizedTest
	@MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#???????????????_3??????")
	@DisplayName("?????????????????? ??????????????? ????????? ??? ??????")
	void ???????????????_??????_??????(SignUpForm providerSignUpForm) {
		// when
		realSignUpService.signUp(providerSignUpForm);

		// then
		User checkedProvider = userRepository.findByUserId(providerSignUpForm.userId()).orElseThrow();
		assertAll(
			() -> assertThat(checkedProvider).isNotNull(),
			() -> assertThat(checkedProvider.userId()).isEqualTo(providerSignUpForm.userId()),
			() -> assertThat(checkedProvider.name()).isEqualTo(providerSignUpForm.userName()),
			() -> assertThat(checkedProvider.email()).isEqualTo(providerSignUpForm.email()),
			() -> assertThat(checkedProvider.userType()).isEqualTo(providerSignUpForm.userType()),
			() -> assertThat(checkedProvider.judgeStatus()).isEqualTo(JudgeStatus.PENDING)
		);
	}
}

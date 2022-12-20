package com.example.flabcaloriecounter.user.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserMapper;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserRepository;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @ParameterizedTest
    @MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
    @DisplayName("중복된 아이디가 있으면 예외를 던진다")
    void signUp_existEmail_fail(final SignUpForm signUpForm) {
        this.signUpService.signUp(signUpForm);

        assertThatThrownBy(() -> this.signUpService.signUp(signUpForm))
                .isInstanceOf(HasDuplicatedIdException.class);
    }

    @ParameterizedTest
    @MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
    @DisplayName("중복된 아이디가 없으면 정상처리한다")
    void signUp_success(final SignUpForm signUpForm) {

        assertDoesNotThrow(() -> this.signUpService.signUp(signUpForm));
    }

    //todo 진형님 작업
//    @ParameterizedTest
//    @MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#정보제공자_3가지")
//    @DisplayName("정보제공자가 성공적으로 가입될 수 있다")
//    void 정보제공자_가입_성공(SignUpForm providerSignUpForm) {
//        // when
//        realSignUpService.signUp(providerSignUpForm);
//
//        // then
//        User checkedProvider = userRepository.findByUserId(providerSignUpForm.userId());
//        assertAll(
//                () -> assertThat(checkedProvider).isNotNull(),
//                () -> assertThat(checkedProvider.userId()).isEqualTo(providerSignUpForm.userId()),
//                () -> assertThat(checkedProvider.name()).isEqualTo(providerSignUpForm.userName()),
//                () -> assertThat(checkedProvider.email()).isEqualTo(providerSignUpForm.email()),
//                () -> assertThat(checkedProvider.userType()).isEqualTo(
//                        providerSignUpForm.userType()),
//                () -> assertThat(checkedProvider.judgeStatus()).isEqualTo(JudgeStatus.PENDING)
//        );
//    }
}

package com.example.flabcaloriecounter.user.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserMapper;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserMybatisRepository;
import com.example.flabcaloriecounter.user.adapter.out.persistence.UserRepository;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @InjectMocks
    SignUpService mockSignUpService;

    @Autowired
    SignUpService realSignUpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Mock
    SignUpPort signUpPort;

    @BeforeEach
    void setup() {
        // UserRepository to UserMybatisRepository
        userRepository = new UserMybatisRepository(userMapper);
    }

    @ParameterizedTest
    @MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
    @DisplayName("중복된 아이디가 있으면 예외를 던진다")
    void signUp_existEmail_fail(SignUpForm signUpForm) {
        when(this.signUpPort.hasDuplicatedId(signUpForm.userId())).thenReturn(true);

        assertThatThrownBy(() -> this.mockSignUpService.signUp(signUpForm))
                .isInstanceOf(HasDuplicatedIdException.class);
    }

    @ParameterizedTest
    @MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#일반사용자_1가지_정보제공자_1가지")
    @DisplayName("중복된 아이디가 없으면 정상처리한다")
    void signUp_success(SignUpForm signUpForm) {
        when(this.signUpPort.hasDuplicatedId(signUpForm.userId())).thenReturn(false);

        assertDoesNotThrow(() -> this.mockSignUpService.signUp(signUpForm));
    }

    @ParameterizedTest
    @MethodSource("com.example.flabcaloriecounter.user.source.TestUserSource#정보제공자_3가지")
    @DisplayName("정보제공자가 성공적으로 가입될 수 있다")
    void 정보제공자_가입_성공(SignUpForm providerSignUpForm) {
        // when
        realSignUpService.signUp(providerSignUpForm);

        // then
        User checkedProvider = userRepository.findByUserId(providerSignUpForm.userId());
        assertAll(
                () -> assertThat(checkedProvider).isNotNull(),
                () -> assertThat(checkedProvider.userId()).isEqualTo(providerSignUpForm.userId()),
                () -> assertThat(checkedProvider.password()).isEqualTo(providerSignUpForm.password()),
                () -> assertThat(checkedProvider.name()).isEqualTo(providerSignUpForm.name()),
                () -> assertThat(checkedProvider.email()).isEqualTo(providerSignUpForm.email()),
                () -> assertThat(checkedProvider.userStatus()).isEqualTo(providerSignUpForm.userStatus())
        );
    }
}

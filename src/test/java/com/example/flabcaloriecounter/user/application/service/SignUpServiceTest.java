package com.example.flabcaloriecounter.user.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.UserStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @InjectMocks
    SignUpService signUpService;

    @Mock
    SignUpPort signUpPort;

    @BeforeEach
    void setup() {}

    @ParameterizedTest
    @MethodSource("TestUserSource#일반사용자_하나_정보제공자_하나")
    @DisplayName("중복된 아이디가 있으면 예외를 던진다")
    void signUp_existEmail_fail(SignUpForm signUpForm) {
        when(this.signUpPort.hasDuplicatedId(signUpForm.userId())).thenReturn(true);

        assertThatThrownBy(() -> this.signUpService.signUp(signUpForm))
                .isInstanceOf(HasDuplicatedIdException.class);
    }

    @ParameterizedTest
    @MethodSource("TestUserSource#일반사용자_하나_정보제공자_하나")
    @DisplayName("중복된 아이디가 없으면 정상처리한다")
    void signUp_success(SignUpForm signUpForm) {
        when(this.signUpPort.hasDuplicatedId(signUpForm.userId())).thenReturn(false);

        assertDoesNotThrow(() -> this.signUpService.signUp(signUpForm));
    }
}

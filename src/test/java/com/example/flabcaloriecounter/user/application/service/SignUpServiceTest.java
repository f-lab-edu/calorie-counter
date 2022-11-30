package com.example.flabcaloriecounter.user.application.service;

import com.example.flabcaloriecounter.exception.AlreadyExistUserIdException;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @InjectMocks
    SignUpService signUpService;

    @Mock
    SignUpPort signUpPort;

    SignUpForm signUpForm;

    @BeforeEach
    void setup() {
        signUpForm = SignUpForm.builder()
                .name("이영진")
                .password("12345678")
                .email("dudwls0505@naver.com")
                .build();
    }

    @Test
    @DisplayName("중복된 아이디가 있으면 예외를던진다")
    void signUp_existEmail_fail() {
        when(signUpPort.isExistId(signUpForm.getUserId())).thenReturn(true);

        assertThatThrownBy(() -> {
            signUpService.signUp(signUpForm);
        }).isInstanceOf(AlreadyExistUserIdException.class);
    }

    @Test
    @DisplayName("중복된 아이디가 없으면 정상처리한다")
    void signUp_success() {
        when(signUpPort.isExistId(signUpForm.getUserId())).thenReturn(false);

        assertDoesNotThrow(() -> {
            signUpService.signUp(signUpForm);
        });
    }
}
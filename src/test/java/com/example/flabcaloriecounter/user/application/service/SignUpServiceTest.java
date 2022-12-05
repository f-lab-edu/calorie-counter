package com.example.flabcaloriecounter.user.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @InjectMocks
    SignUpService signUpService;

    @Mock
    SignUpPort signUpPort;

    SignUpForm signUpForm;

    @BeforeEach
    void setup() {
        this.signUpForm = new SignUpForm(
                "asdf123545",
                "이영진",
                "12345678",
                "dudwls0505@naver.com"
        );
    }

    @Test
    @DisplayName("중복된 아이디가 있으면 예외를던진다")
    void signUp_existEmail_fail() {
        when(this.signUpPort.hasDuplicatedId(this.signUpForm.userId())).thenReturn(true);

        assertThatThrownBy(() -> this.signUpService.signUp(this.signUpForm))
                .isInstanceOf(HasDuplicatedIdException.class);
    }

    @Test
    @DisplayName("중복된 아이디가 없으면 정상처리한다")
    void signUp_success() {
        when(this.signUpPort.hasDuplicatedId(signUpForm.userId())).thenReturn(false);

        assertDoesNotThrow(() -> this.signUpService.signUp(this.signUpForm));
    }
}
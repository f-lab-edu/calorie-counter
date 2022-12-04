package com.example.flabcaloriecounter.user.adapter.in.web;

import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignUpUseCase signUpUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() throws Exception {
        SignUpForm user = SignUpForm.builder()
                .userId("asdf1234124213")
                .name("이영진")
                .password("asdfsa5678")
                .email("asdf123456@naver.com")
                .build();

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.rightUserForm)))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(this.signUpUseCase).signUp(any(SignUpForm.class));
    }

    @Test
    @DisplayName("잘못된 Form 입력값으로 회원가입 실패")
    void signUp_wrong_input_fail() throws Exception {
        SignUpForm user = SignUpForm.builder()
                .userId("123131413")
                .name("1")
                .password("1")
                .email("2")
                .build();

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.wrongUserForm)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(this.signUpUseCase, never()).signUp(any(SignUpForm.class));
    }
}
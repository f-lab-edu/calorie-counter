package com.example.flabcaloriecounter.user.adapter.in.web;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.ARGUMENT_NOT_VALID_MSG;
import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.HAS_DUPLICATED_ID_MSG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignUpUseCase signUpUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    SignUpForm rightUserForm;
    SignUpForm wrongUserForm;

    @BeforeEach
    void setup() {
        rightUserForm = new SignUpForm(
            "rightUserId",
            "올바른유저",
            "12345678",
            "dudwls0505@naver.com",
            60.03,
            UserStatus.ORDINARY,
            JudgeStatus.getInitialJudgeStatusByUserStatus(UserStatus.ORDINARY)
        );

        wrongUserForm = new SignUpForm(
            "wrongUserId",
            "잘못된유저",
            "1",
            "2",
            70.02,
            UserStatus.ORDINARY,
            JudgeStatus.getInitialJudgeStatusByUserStatus(UserStatus.ORDINARY)
        );
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() throws Exception {
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.rightUserForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("userId").value("rightUserId"))
                .andExpect(jsonPath("name").value("올바른유저"))
                .andExpect(jsonPath("password").value("12345678"))
                .andExpect(jsonPath("email").value("dudwls0505@naver.com"))
                .andDo(print());

        verify(this.signUpUseCase).signUp(any(SignUpForm.class));
    }

    @Test
    @DisplayName("잘못된 Form 입력값으로 회원가입 실패")
    void signUp_wrong_input_fail() throws Exception {
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.wrongUserForm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ARGUMENT_NOT_VALID_MSG))
                .andExpect(jsonPath("statusCode").value("BAD_REQUEST"))
                .andDo(print());

        verify(this.signUpUseCase, never()).signUp(any(SignUpForm.class));
    }

    @Test
    @DisplayName("중복 회원가입 시도시 AlreadyExistUserIdException 예외를 던진다.")
    void signUp_AlreadyExistUserIdException_fail() throws Exception {
        doThrow(HasDuplicatedIdException.class).when(this.signUpUseCase)
                .signUp(any(SignUpForm.class));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.rightUserForm)))
                .andDo(print())
                .andExpect(jsonPath("message").value(HAS_DUPLICATED_ID_MSG))
                .andExpect(jsonPath("statusCode").value("CONFLICT"))
                .andExpect(status().isConflict());
    }
}

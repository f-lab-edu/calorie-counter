package com.example.flabcaloriecounter.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private SignUpForm signUpForm;

    @BeforeEach
    void setup() {
        this.signUpForm = new SignUpForm(
                "asdf123545",
                "이영진",
                "12345678",
                "dudwls0505@naver.com",
                60.02,
                UserType.ORDINARY,
                JudgeStatus.getInitialJudgeStatusByUserType(UserType.ORDINARY)
        );

        userMapper.signUp(this.signUpForm);
    }

    @Test
    @DisplayName("회원가입 정상처리 테스트")
    void signUp() {
        assertThat(this.userMapper.hasDuplicatedId(this.signUpForm.userId())).isTrue();
        assertThat(this.userMapper.hasDuplicatedId("tempId")).isFalse();
    }
}

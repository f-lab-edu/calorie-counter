package com.example.flabcaloriecounter.user.adapter.out.persistence;

import com.example.flabcaloriecounter.config.MybatisConfig;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Import(MybatisConfig.class)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private SignUpForm signUpForm;

    @BeforeEach
    void setup() {
        signUpForm = SignUpForm.builder()
                .userId("asdfasdasd123")
                .name("이영진")
                .password("asdfsa5678")
                .email("asdf123456@naver.com")
                .build();

        userMapper.signUp(signUpForm);
    }

    @Test
    @DisplayName("회원가입 정상처리 테스트")
    void signUp() {
        assertThat(userMapper.isExistId(signUpForm.getUserId())).isTrue();
        assertThat(userMapper.isExistId("tempId")).isFalse();
    }
}
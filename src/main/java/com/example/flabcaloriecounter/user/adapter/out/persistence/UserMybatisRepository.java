package com.example.flabcaloriecounter.user.adapter.out.persistence;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.util.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMybatisRepository implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public void signUp(SignUpForm signUpForm) {
        SignUpForm user = SignUpForm.builder()
                .userId(signUpForm.getUserId())
                .name(signUpForm.getName())
                .password(PasswordEncrypt.encrypt(signUpForm.getPassword()))
                .email(signUpForm.getEmail())
                .build();

        userMapper.signUp(user);
    }

    @Override
    public boolean isExistId(String userId) {
        return userMapper.isExistId(userId);
    }
}

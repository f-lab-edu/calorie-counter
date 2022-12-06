package com.example.flabcaloriecounter.user.adapter.out.persistence;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMybatisRepository implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public void signUp(final SignUpForm signUpForm) {
        SignUpForm user = new SignUpForm(
                signUpForm.userId(),
                signUpForm.name(),
                PasswordEncrypt.encrypt(signUpForm.password()),
                signUpForm.email(),
                signUpForm.userStatus()
        );

        this.userMapper.signUp(user);
    }

    @Override
    public boolean hasDuplicatedId(final String userId) {
        return this.userMapper.hasDuplicatedId(userId);
    }

    @Override
    public User findByUserId(final String userId) {
        return this.userMapper.findByUserId(userId);
    }
}

package com.example.flabcaloriecounter.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.PasswordEncrypt;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMybatisRepository implements UserRepository {

	private final UserMapper userMapper;

	@Override
	public void signUp(final SignUpForm signUpForm) {
		signUpForm.setUserPassword(PasswordEncrypt.encrypt(signUpForm.getUserPassword()));
		this.userMapper.signUp(signUpForm);
	}

	@Override
	public boolean hasDuplicatedId(final String userId) {
		return this.userMapper.hasDuplicatedId(userId);
	}

	@Override
	public Optional<User> findByUserId(final String userId) {
		return this.userMapper.findByUserId(userId);
	}

	@Override
	public String getPassword(final String userId) {
		return this.userMapper.getPassword(userId);
	}
}

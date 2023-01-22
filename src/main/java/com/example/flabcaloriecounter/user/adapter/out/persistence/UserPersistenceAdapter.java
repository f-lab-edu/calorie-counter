package com.example.flabcaloriecounter.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.LoginPort;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SignUpPort, LoginPort {

	private final UserRepository userRepository;

	@Override
	public void signUp(final SignUpForm signUpForm) {
		this.userRepository.signUp(signUpForm);
	}

	@Override
	public boolean hasDuplicatedId(final String userId) {
		return this.userRepository.hasDuplicatedId(userId);
	}

	@Override
	public Optional<User> findByUserId(final String mockUserId) {
		return this.userRepository.findByUserId(mockUserId);
	}

	@Override
	public String getPassword(final String userId) {
		return this.userRepository.getPassword(userId);
	}
}

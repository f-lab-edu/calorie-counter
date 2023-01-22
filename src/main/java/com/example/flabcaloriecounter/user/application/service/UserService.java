package com.example.flabcaloriecounter.user.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.exception.PasswordNotMatchException;
import com.example.flabcaloriecounter.exception.UserNotFoundException;
import com.example.flabcaloriecounter.user.application.port.in.UserUseCase;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.LoginPort;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.PasswordEncrypt;
import com.example.flabcaloriecounter.util.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

	private final SignUpPort signUpPort;
	private final LoginPort loginPort;
	private final TokenService tokenService;
	private final UserCacheService userCacheService;

	@Override
	@Transactional
	public void signUp(final SignUpForm signUpForm) {
		if (this.signUpPort.hasDuplicatedId(signUpForm.userId())) {
			throw new HasDuplicatedIdException(String.format("%s has duplicated Id", signUpForm.userId()),
				"이미 존재하는 ID 입니다");
		}

		this.signUpPort.signUp(signUpForm);
	}

	@Override
	@Transactional
	public ResponseIssuedToken login(final LoginForm loginForm) {
		final User user = this.findByUserId(loginForm.userId())
			.orElseThrow(() -> new UserNotFoundException(String.format("userId %s not exist", loginForm.userId())));

		if (!PasswordEncrypt.isMatch(loginForm.userPassword(), this.loginPort.getPassword(loginForm.userId()))) {
			throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
		}

		//todo 로그인 5회 실패시 lock, Response
		this.userCacheService.setUser(user);
		return this.tokenService.issue(loginForm.userId());
	}

	@Override
	public Optional<User> findByUserId(final String userId) {
		return Optional.ofNullable(
			this.userCacheService.getUser(userId).orElseGet(() -> this.signUpPort.findByUserId(userId).orElse(null)
			));
	}
}

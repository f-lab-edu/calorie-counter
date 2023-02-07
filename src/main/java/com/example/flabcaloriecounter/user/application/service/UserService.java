package com.example.flabcaloriecounter.user.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.user.application.port.in.UserUseCase;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.LoginPort;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.PasswordEncrypt;
import com.example.flabcaloriecounter.util.StatusEnum;
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
		if (this.signUpPort.hasDuplicatedId(signUpForm.getUserId())) {
			throw new CustomException(StatusEnum.DUPLICATED_ID,
				String.format("%s has duplicated Id", signUpForm.getUserId()));
		}
		this.signUpPort.signUp(signUpForm);
	}

	@Override
	@Transactional
	public ResponseIssuedToken login(final LoginForm loginForm) {
		final User user = this.findByUserId(loginForm.userId())
			.orElseThrow(() -> new CustomException(
				StatusEnum.USER_NOT_FOUND, String.format("userId %s not exist", loginForm.userId())));

		if (!PasswordEncrypt.isMatch(loginForm.userPassword(), this.loginPort.getPassword(loginForm.userId()))) {
			throw new CustomException(StatusEnum.PASSWORD_NOT_MATCH);
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

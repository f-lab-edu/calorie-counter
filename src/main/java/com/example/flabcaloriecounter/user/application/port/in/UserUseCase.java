package com.example.flabcaloriecounter.user.application.port.in;

import java.util.Optional;

import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.User;

public interface UserUseCase {

	void signUp(final SignUpForm signUpForm);

	ResponseIssuedToken login(final LoginForm loginForm);

	Optional<User> findByUserId(final String userId);
}

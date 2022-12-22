package com.example.flabcaloriecounter.user.application.port.out;

import java.util.Optional;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.User;

public interface SignUpPort {

	void signUp(final SignUpForm signUpForm);

	boolean hasDuplicatedId(final String userId);

	Optional<User> findByUserId(final String mockUserId);
}

package com.example.flabcaloriecounter.user.adapter.out.persistence;

import java.util.Optional;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.User;

public interface UserRepository {

	void signUp(final SignUpForm signUpForm);

	boolean hasDuplicatedId(final String userId);

	Optional<User> findByUserId(final String userId);
}

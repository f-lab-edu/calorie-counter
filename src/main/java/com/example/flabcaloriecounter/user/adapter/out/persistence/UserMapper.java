package com.example.flabcaloriecounter.user.adapter.out.persistence;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.User;

@Mapper
public interface UserMapper {

	void signUp(final SignUpForm signUpForm);

	boolean hasDuplicatedId(final String userId);

	Optional<User> findByUserId(final String userId);

	String getPassword(final String userId);
}

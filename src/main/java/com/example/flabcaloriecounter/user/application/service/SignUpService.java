package com.example.flabcaloriecounter.user.application.service;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.HAS_DUPLICATED_ID_MSG;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

	private final SignUpPort signUpPort;

	@Override
	@Transactional
	public void signUp(final SignUpForm signUpForm) {
		if (this.signUpPort.hasDuplicatedId(signUpForm.userId())) {
			throw new HasDuplicatedIdException(HAS_DUPLICATED_ID_MSG);
		}

		this.signUpPort.signUp(signUpForm);
	}

	public Optional<User> findByUserId(final String mockUserId) {
		return this.signUpPort.findByUserId(mockUserId);
	}
}

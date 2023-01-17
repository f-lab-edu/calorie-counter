package com.example.flabcaloriecounter.user.adapter.in.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.flabcaloriecounter.user.application.port.in.UserUseCase;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserUseCase userUseCase;

	@PostMapping("/users")
	public ResponseEntity<SignUpForm> signUpSubmit(
		@RequestBody @Valid final SignUpForm signUpForm) {
		this.userUseCase.signUp(signUpForm);
		return new ResponseEntity<>(signUpForm, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseToken> login(@RequestBody @Valid LoginForm loginForm) {
		return new ResponseEntity<>(this.userUseCase.login(loginForm), HttpStatus.OK);
	}
}
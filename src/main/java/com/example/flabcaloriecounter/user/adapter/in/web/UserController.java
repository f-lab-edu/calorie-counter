package com.example.flabcaloriecounter.user.adapter.in.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.flabcaloriecounter.user.application.port.in.UserUseCase;
import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.util.CustomResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserUseCase userUseCase;

	@PostMapping("/users")
	public CustomResponse<SignUpForm> signUpSubmit(
		@RequestBody @Valid final SignUpForm signUpForm) {
		this.userUseCase.signUp(signUpForm);
		return CustomResponse.success(signUpForm);
	}

	@PostMapping("/login")
	public CustomResponse<ResponseIssuedToken> login(@RequestBody @Valid LoginForm loginForm) {
		return CustomResponse.success(this.userUseCase.login(loginForm));
	}
}
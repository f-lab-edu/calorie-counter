package com.example.flabcaloriecountergateway.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestLoginDto(
	@NotNull(message = "아이디를 입력해주세요.")
	@Size(min = 1, max = 20, message = "아이디는 1자 이상 20자 이하로 입력해주세요.")
	String id,

	@NotNull(message = "비밀번호를 입력해주세요.")
	@Size(min = 1, max = 20, message = "비밀번호는 1자 이상 20자 이하로 입력해주세요.")
	String password
) {
}

package com.example.flabcaloriecounter.user.application.port.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record LoginForm(
	@NotBlank @Size(min = 6, max = 20) @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$")
	String userId,
	@NotBlank @Size(min = 8, max = 25) @Pattern(regexp = "^[a-zA-Z0-9]+$")
	String userPassword
) {
}

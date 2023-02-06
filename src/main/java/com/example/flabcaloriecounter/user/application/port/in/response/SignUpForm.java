package com.example.flabcaloriecounter.user.application.port.in.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignUpForm {
	//mybatis 에서 주입받기위한 용도
	private final long id;

	@NotBlank
	@Size(min = 6, max = 20)
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$")
	private final String userId;
	@NotBlank
	@Size(min = 1, max = 20)
	@Pattern(regexp = "^[가-힣|a-zA-Z]+$")
	private final String userName;
	@NotBlank
	@Size(min = 8, max = 60)
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()/.,:';]+$")
	private String userPassword;
	@NotBlank
	@Email
	private final String email;

	public SignUpForm() {
		this(1, "", "", "", "");
	}

	public SignUpForm(long id, String userId, String userName, String userPassword, String email) {
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.email = email;
	}

	public SignUpForm(long id, String userId, String userName, String email) {
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.email = email;
	}

	public SignUpForm(String userId, String userName, String userPassword, String email) {
		this(1, userId, userName, userPassword, email);
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}

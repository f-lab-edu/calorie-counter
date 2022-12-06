package com.example.flabcaloriecounter.user.source;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.UserStatus;

public class TestUserSource {
	static final SignUpForm 일반사용자_가입폼_케이스1 = new SignUpForm(
		"asdf123545",
		"이영진",
		"12345678",
		"dudwls0505@naver.com",
		50.3,
		UserStatus.ORDINARY
	);

	static final SignUpForm 정보제공자_가입폼_케이스1 = new SignUpForm(
		"provider123",
		"정보제공자_이름",
		"12345678",
		"provider@gmail.com",
		60.02,
		UserStatus.PROVIDER
	);

	static final SignUpForm 정보제공자_가입폼_케이스2 = new SignUpForm(
		"provider5334",
		"정보제공자_이름2",
		"87837823",
		"provider1234@gmail.com",
		61.03,
		UserStatus.PROVIDER
	);

	static final SignUpForm 정보제공자_가입폼_케이스3 = new SignUpForm(
		"provider5334",
		"정보제공자_이름2",
		"87837823",
		"provider8232@gmail.com",
		65.05,
		UserStatus.PROVIDER
	);

	public static Stream<Arguments> 일반사용자_1가지_정보제공자_1가지() {
		return Stream.of(
			Arguments.of(일반사용자_가입폼_케이스1),
			Arguments.of(정보제공자_가입폼_케이스1)
		);
	}

	public static Stream<Arguments> 정보제공자_3가지() {
		return Stream.of(
			Arguments.of(정보제공자_가입폼_케이스1),
			Arguments.of(정보제공자_가입폼_케이스2),
			Arguments.of(정보제공자_가입폼_케이스3)
		);
	}
}

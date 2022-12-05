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
		UserStatus.USER
	);

	static final SignUpForm 정보제공자_가입폼_케이스2 = new SignUpForm(
		"provider123",
		"정보제공자_이름",
		"12345678",
		"provider@gmail.com",
		UserStatus.PROVIDER
	);

	public static Stream<Arguments> 일반사용자_하나_정보제공자_하나() {
		return Stream.of(
			Arguments.of(일반사용자_가입폼_케이스1),
			Arguments.of(정보제공자_가입폼_케이스2)
		);
	}
}

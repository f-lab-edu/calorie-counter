package com.example.flabcaloriecounter.user.source;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.example.flabcaloriecounter.user.application.port.in.dto.LoginForm;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserType;

public class TestUserSource {

	private static final String RIGHT_USER_ID = "rightUserId";
	private static final String RIGHT_PASSWORD = "12345678";
	private static final String WRONG_PASSWORD = "4555qrqqweqwd";

	static final SignUpForm 일반사용자_가입폼_케이스1 = new SignUpForm(
		"asdf123545",
		"이영진",
		"12345678",
		"dudwls0505@naver.com",
		50.3,
		UserType.ORDINARY,
		JudgeStatus.getInitialJudgeStatusByUserType(UserType.ORDINARY)
	);

	static final SignUpForm 정보제공자_가입폼_케이스1 = new SignUpForm(
		"provider123",
		"정보제공자_이름",
		"12345678",
		"provider@gmail.com",
		60.02,
		UserType.PROVIDER,
		JudgeStatus.getInitialJudgeStatusByUserType(UserType.PROVIDER)
	);

	static final SignUpForm 정보제공자_가입폼_케이스2 = new SignUpForm(
		"provider5334",
		"정보제공자_이름2",
		"87837823",
		"provider1234@gmail.com",
		61.03,
		UserType.PROVIDER,
		JudgeStatus.getInitialJudgeStatusByUserType(UserType.PROVIDER)
	);

	static final SignUpForm 정보제공자_가입폼_케이스3 = new SignUpForm(
		"provider5334",
		"정보제공자_이름2",
		"87837823",
		"provider8232@gmail.com",
		65.05,
		UserType.PROVIDER,
		JudgeStatus.getInitialJudgeStatusByUserType(UserType.PROVIDER)
	);

	public static final SignUpForm rightSignUpForm = new SignUpForm(
		RIGHT_USER_ID,
		"올바른유저",
		RIGHT_PASSWORD,
		"dudwls0505@naver.com",
		60.03,
		UserType.ORDINARY,
		JudgeStatus.getInitialJudgeStatusByUserType(UserType.ORDINARY)
	);

	public static final SignUpForm wrongSignUpForm = new SignUpForm(
		"wrongUserId",
		"잘못된유저",
		"1",
		"2",
		70.02,
		UserType.ORDINARY,
		JudgeStatus.getInitialJudgeStatusByUserType(UserType.ORDINARY)
	);

	public static final LoginForm rightLoginForm = new LoginForm(
		RIGHT_USER_ID,
		RIGHT_PASSWORD
	);

	public static final LoginForm wrongLoginForm = new LoginForm(
		"wrongUserId",
		"1"
	);

	public static final LoginForm wrongPasswordLoginForm = new LoginForm(
		RIGHT_USER_ID,
		WRONG_PASSWORD
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

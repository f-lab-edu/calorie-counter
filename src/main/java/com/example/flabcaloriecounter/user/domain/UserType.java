package com.example.flabcaloriecounter.user.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
	ORDINARY("01", "일반"),
	PROVIDER("02", "제공자"),
	ADMIN("03", "관리자");

	private final String code;
	private final String statusMessage;

	public boolean checkUserCode(String code) {
		return this.code.equals(code);
	}

	public static UserType findByMessage(final String code) {
		return Arrays.stream(UserType.values())
			.filter(getUserCode(code))
			.findFirst()
			.orElseThrow(NonCodeFoundErrorSupplier());
	}

	private static Supplier<IllegalArgumentException> NonCodeFoundErrorSupplier() {
		return () -> new IllegalArgumentException("해당하는 상태가 없습니다.");
	}

	private static Predicate<UserType> getUserCode(String code) {
		return userType -> userType.checkUserCode(code);
	}

	public boolean isProvider() {
		return this == PROVIDER;
	}
}

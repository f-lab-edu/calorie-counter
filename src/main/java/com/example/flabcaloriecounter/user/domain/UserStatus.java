package com.example.flabcaloriecounter.user.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
	WITHDRAWAL("01", "탈퇴상태"),
	NOT_WITHDRAWAL("02", "정상상태");

	private final String code;
	private final String statusMessage;

	public boolean checkUserCode(String code) {
		return this.code.equals(code);
	}

	public static UserStatus findByMessage(String code) {
		return Arrays.stream(UserStatus.values())
			.filter(getUserCode(code))
			.findFirst()
			.orElseThrow(NonCodeFoundErrorSupplier());
	}

	private static Supplier<IllegalArgumentException> NonCodeFoundErrorSupplier() {
		return () -> new IllegalArgumentException("해당하는 상태가 없습니다.");
	}

	private static Predicate<UserStatus> getUserCode(String code) {
		return userStatus -> userStatus.checkUserCode(code);
	}
}

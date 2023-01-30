package com.example.flabcaloriecounter.user.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum UserStatus {
	WITHDRAWAL("01", "탈퇴상태"),
	NOT_WITHDRAWAL("02", "정상상태");

	private String code;
	private String statusMessage;

	UserStatus(String code, String statusMessage) {
		this.code = code;
		this.statusMessage = statusMessage;
	}

	public String getCode() {
		return this.code;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

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

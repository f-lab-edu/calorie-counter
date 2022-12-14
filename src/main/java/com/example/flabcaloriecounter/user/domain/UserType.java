package com.example.flabcaloriecounter.user.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum UserType {
	ORDINARY("일반"),
	PROVIDER("제공자"),
	ADMIN("관리자");

	private final String statusMessage;

	UserType(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public boolean checkStatusMessage(String statusMessage) {
		return this.statusMessage.equals(statusMessage);
	}

	public static UserType findByMessage(final String statusMessage) {
		return Arrays.stream(UserType.values())
			.filter(getUserStatusPredicate(statusMessage))
			.findFirst()
			.orElseThrow(NonStatusFoundErrorSupplier());
	}

	private static Supplier<IllegalArgumentException> NonStatusFoundErrorSupplier() {
		return () -> new IllegalArgumentException("해당하는 상태가 없습니다.");
	}

	private static Predicate<UserType> getUserStatusPredicate(String statusMessage) {
		return userType -> userType.checkStatusMessage(statusMessage);
	}

	public boolean isProvider() {
		return this == PROVIDER;
	}
}

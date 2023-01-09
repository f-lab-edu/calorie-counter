package com.example.flabcaloriecounter.feed.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum LikeStatus {
	ACTIVATE("활성화"),
	NOT_ACTIVATE("비활성화");

	private final String statusMessage;

	LikeStatus(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public boolean checkStatusMessage(String statusMessage) {
		return this.statusMessage.equals(statusMessage);
	}

	public static LikeStatus findByMessage(final String statusMessage) {
		return Arrays.stream(LikeStatus.values())
			.filter(getUserStatusPredicate(statusMessage))
			.findFirst()
			.orElseThrow(NonStatusFoundErrorSupplier());
	}

	private static Supplier<IllegalArgumentException> NonStatusFoundErrorSupplier() {
		return () -> new IllegalArgumentException("해당하는 상태가 없습니다.");
	}

	private static Predicate<LikeStatus> getUserStatusPredicate(String statusMessage) {
		return LikeStatus -> LikeStatus.checkStatusMessage(statusMessage);
	}
}

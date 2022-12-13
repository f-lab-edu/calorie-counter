package com.example.flabcaloriecounter.user.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum UserStatus {
    WITHDRAWAL("탈퇴상태"),
    NOT_WITHDRAWAL("정상상태");

    private String statusMessage;

    UserStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public boolean checkStatusMessage(String statusMessage) {
        return this.statusMessage.equals(statusMessage);
    }

    public static UserStatus findByMessage(String statusMessage) {
        return Arrays.stream(UserStatus.values())
                .filter(getUserStatusPredicate(statusMessage))
                .findFirst()
                .orElseThrow(NonStatusFoundErrorSupplier());
    }

    private static Supplier<IllegalArgumentException> NonStatusFoundErrorSupplier() {
        return () -> new IllegalArgumentException("해당하는 상태가 없습니다.");
    }

    private static Predicate<UserStatus> getUserStatusPredicate(String statusMessage) {
        return userStatus -> userStatus.checkStatusMessage(statusMessage);
    }
}

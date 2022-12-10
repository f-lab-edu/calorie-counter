package com.example.flabcaloriecounter.user.domain;

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

    public static UserStatus findByMessage(String statusMessage) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.getStatusMessage().equals(statusMessage)) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException("해당하는 상태가 없습니다.");
    }
}

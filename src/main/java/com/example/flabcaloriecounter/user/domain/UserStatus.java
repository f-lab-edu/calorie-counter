package com.example.flabcaloriecounter.user.domain;

public enum UserStatus {
    WITHDRAWAL(0),
    NOT_WITHDRAWAL(1);

    private int statusCode;

    UserStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static UserStatus valueOf(int value) {
        for (UserStatus userStatus : values()) {
            if (userStatus.getStatusCode() == value) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus code: " + value);
    }
}

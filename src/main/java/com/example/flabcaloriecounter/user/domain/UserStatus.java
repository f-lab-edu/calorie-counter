package com.example.flabcaloriecounter.user.domain;

public enum UserStatus {
    ORDINARY, PROVIDER, ADMIN;

    public boolean isProvider() {
        return this == PROVIDER;
    }
}

package com.example.flabcaloriecounter.user.domain;

public enum UserType {
    ORDINARY, PROVIDER, ADMIN;

    public boolean isProvider() {
        return this == PROVIDER;
    }
}

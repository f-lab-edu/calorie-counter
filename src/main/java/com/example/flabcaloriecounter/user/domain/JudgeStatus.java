package com.example.flabcaloriecounter.user.domain;

public enum JudgeStatus {
    PENDING, PASSED, REJECTED, NOT_ASSIGNED;

    public static JudgeStatus getInitialJudgeStatusByUserType(UserType userType) {
        if (userType.isProvider()) {
            return JudgeStatus.PENDING;
        }
        return JudgeStatus.NOT_ASSIGNED;
    }
}

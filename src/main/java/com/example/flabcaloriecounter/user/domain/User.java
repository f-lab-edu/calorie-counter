package com.example.flabcaloriecounter.user.domain;

import java.time.LocalDateTime;

public record User(
        String userId,
        String name,
        String password,
        String email,
        double weight,
        String withdrawalReason,
        LocalDateTime joinDate,
        WithdrawalStatus withdrawalStatus,
        JudgeStatus judgeStatus,
        UserStatus userStatus,
        String photoLink
) {

}

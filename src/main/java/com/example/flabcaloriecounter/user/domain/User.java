package com.example.flabcaloriecounter.user.domain;

import java.time.LocalDateTime;

public record User(
        String userId,
        String name,
        String password,
        String email,
        Double weight,
        String withdrawalReason,
        LocalDateTime joinDate,
		UserStatus status,
		UserType userType,
		JudgeStatus judgeStatus,
		String photoLink
) {

}

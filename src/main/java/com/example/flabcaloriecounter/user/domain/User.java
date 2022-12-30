package com.example.flabcaloriecounter.user.domain;

import java.time.LocalDateTime;

public record User(
	long id,
	String userId,
	String name,
	String password,
	String email,
	Double weight,
	String withdrawalReason,
	LocalDateTime joinDate,
	UserStatus userStatus,
	UserType userType,
	JudgeStatus judgeStatus,
	String photoLink
) {

}

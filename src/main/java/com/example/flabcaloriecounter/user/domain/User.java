package com.example.flabcaloriecounter.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Builder @AllArgsConstructor
public class User {

    private final String UserId;

    private final String name;

    private final String password;

    private final String email;

    private final double weight;

    private final String withdrawalReason;

    private final LocalDateTime joinDate;

    private final WithdrawalStatus withdrawalStatus;

    private final JudgeStatus judgeStatus;

    private final UserStatus userStatus;

    private final String photoLink;
}

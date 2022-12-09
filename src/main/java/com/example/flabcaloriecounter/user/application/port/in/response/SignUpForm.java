package com.example.flabcaloriecounter.user.application.port.in.response;

import com.example.flabcaloriecounter.user.domain.JudgeStatus;
import com.example.flabcaloriecounter.user.domain.UserType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

public record SignUpForm(
        @NotBlank @Size(min = 6, max = 20) @Pattern(regexp = userIdPattern)
        String userId,
        @NotBlank @Size(min = 1, max = 20) @Pattern(regexp = namePattern)
        String userName,
        @NotBlank @Size(min = 8, max = 25) @Pattern(regexp = passwordPattern)
        String userPassword,
        @NotBlank @Email
        String email,

        @NotNull @NumberFormat
        Double weight,

        @NotNull
        UserType userType,

        @NotNull
        JudgeStatus judgeStatus
) {

    public static final String userIdPattern = "^[a-zA-Z0-9가-힣]+$";
    public static final String namePattern = "^[가-힣|a-zA-Z]+$";
    public static final String passwordPattern = "^[a-zA-Z0-9]+$";
}

package com.example.flabcaloriecounter.user.application.port.in.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record SignUpForm(
        @NotBlank @Size(min = 6, max = 20) @Pattern(regexp = userIdPattern)
        String userId,
        @NotBlank @Size(min = 1, max = 20) @Pattern(regexp = namePattern)
        String name,
        @NotBlank @Size(min = 8, max = 25) @Pattern(regexp = passwordPattern)
        String password,
        @NotBlank @Email
        String email
) {

    public static final String userIdPattern = "^[a-zA-Z0-9가-힣]+$";
    public static final String namePattern = "^[가-힣|a-zA-Z]+$";
    public static final String passwordPattern = "^[a-zA-Z0-9]+$";
}

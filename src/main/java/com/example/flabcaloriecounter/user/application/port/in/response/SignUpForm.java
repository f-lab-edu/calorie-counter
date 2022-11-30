package com.example.flabcaloriecounter.user.application.port.in.response;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SignUpForm {

    @NotBlank
    @Size(min = 6, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$")
    private String userId;

    @NotBlank
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[가-힣|a-zA-Z]+$")
    private String name;

    @NotBlank
    @Size(min = 8, max = 25)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String password;

    @NotBlank @Email
    private String email;
}

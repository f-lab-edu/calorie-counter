package com.example.flabcaloriecounter.user.application.port.out;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;

public interface SignUpPort {
    void signUp(SignUpForm signUpForm);

    boolean isExistId(String userId);
}

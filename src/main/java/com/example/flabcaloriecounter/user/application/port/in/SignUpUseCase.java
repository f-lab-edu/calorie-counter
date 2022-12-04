package com.example.flabcaloriecounter.user.application.port.in;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;

public interface SignUpUseCase {

    void signUp(final SignUpForm signUpForm);
}

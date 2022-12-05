package com.example.flabcaloriecounter.user.adapter.out.persistence;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;

public interface UserRepository {

    void signUp(final SignUpForm signUpForm);

    boolean hasDuplicatedId(final String userId);
}

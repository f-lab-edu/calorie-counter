package com.example.flabcaloriecounter.user.adapter.out.persistence;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SignUpPort {

    private final UserRepository userRepository;

    @Override
    public void signUp(final SignUpForm signUpForm) {
        this.userRepository.signUp(signUpForm);
    }

    @Override
    public boolean hasDuplicatedId(final String userId) {
        return this.userRepository.hasDuplicatedId(userId);
    }
}

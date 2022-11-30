package com.example.flabcaloriecounter.user.adapter.out.persistence;

import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SignUpPort {

    private final UserRepository userRepository;

    @Override
    public void signUp(SignUpForm signUpForm) {
        userRepository.signUp(signUpForm);
    }

    @Override
    public boolean isExistId(String userId) {
        return userRepository.isExistId(userId);
    }
}

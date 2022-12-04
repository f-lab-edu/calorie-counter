package com.example.flabcaloriecounter.user.application.service;

import com.example.flabcaloriecounter.exception.HasDuplicatedIdException;
import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final SignUpPort signUpPort;

    @Override
    @Transactional
    public void signUp(final SignUpForm signUpForm) {
        if (this.signUpPort.hasDuplicatedId(signUpForm.userId())) {
            throw new HasDuplicatedIdException("이미 존재하는 ID입니다");
        }

        this.signUpPort.signUp(signUpForm);
    }
}

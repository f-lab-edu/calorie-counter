package com.example.flabcaloriecounter.user.application.service;

import com.example.flabcaloriecounter.exception.AlreadyExistUserIdException;
import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.out.SignUpPort;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final SignUpPort signUpPort;

    @Override
    @Transactional
    public void signUp(SignUpForm signUpForm) {
        if (signUpPort.isExistId(signUpForm.getUserId())) {
            throw new AlreadyExistUserIdException("이미 존재하는 ID입니다");
        }

        signUpPort.signUp(signUpForm);
    }
}

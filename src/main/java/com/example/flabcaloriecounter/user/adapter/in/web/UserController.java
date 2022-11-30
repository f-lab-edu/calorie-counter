package com.example.flabcaloriecounter.user.adapter.in.web;

import com.example.flabcaloriecounter.exception.AlreadyExistUserIdException;
import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final SignUpUseCase signUpUseCase;

    @PostMapping("/users")
    public ResponseEntity<String> signUpSubmit(@RequestBody @Valid SignUpForm signUpForm) {
        try {
            //TODO 제공자 어떻게 가입할건지 논의
            signUpUseCase.signUp(signUpForm);
        } catch (AlreadyExistUserIdException e) {
            return new ResponseEntity<>("중복된 아이디", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("성공", HttpStatus.CREATED);
    }
}

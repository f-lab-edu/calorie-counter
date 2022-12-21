package com.example.flabcaloriecounter.user.adapter.in.web;

import static com.example.flabcaloriecounter.user.adapter.in.web.Constants.*;

import com.example.flabcaloriecounter.user.application.port.in.SignUpUseCase;
import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final SignUpUseCase signUpUseCase;

    @PostMapping(LATEST_API_VERSION + "/users")
    public ResponseEntity<SignUpForm> signUpSubmit(
            @RequestHeader("x-user-id") @RequestBody @Valid final SignUpForm signUpForm) {
        this.signUpUseCase.signUp(signUpForm);
        return new ResponseEntity<>(signUpForm, HttpStatus.CREATED);
    }
}

package com.example.flabcaloriecounter.user.adapter.out.persistence;

import com.example.flabcaloriecounter.user.application.port.in.response.SignUpForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void signUp(final SignUpForm signUpForm);

    boolean hasDuplicatedId(final String userId);
}

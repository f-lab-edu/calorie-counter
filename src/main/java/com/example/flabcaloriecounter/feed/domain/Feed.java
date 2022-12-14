package com.example.flabcaloriecounter.feed.domain;

import com.example.flabcaloriecounter.user.domain.User;
import java.util.List;

public record Feed(
        String contents,
        User user,
        List<Photo> photo
) {

}

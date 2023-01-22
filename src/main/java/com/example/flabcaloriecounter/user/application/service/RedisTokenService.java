package com.example.flabcaloriecounter.user.application.service;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTokenService {

	private final RedisTemplate<String, String> tokenRedisTemplate;
	private static final Duration REFRESH_TOKEN_TIMEOUT = Duration.ofDays(14);

	public void setToken(final String userId, final String refreshToken) {
		tokenRedisTemplate.opsForValue().set(getKey(userId), refreshToken, REFRESH_TOKEN_TIMEOUT);
	}

	public Optional<String> getRefreshToken(final String userId) {
		return Optional.ofNullable(tokenRedisTemplate.opsForValue().get(getKey(userId)));
	}

	private String getKey(final String userId) {
		return "tokenUserId = " + userId;
	}

	public void deleteAll() {
		tokenRedisTemplate.delete(Objects.requireNonNull(tokenRedisTemplate.keys("*")));
	}
}

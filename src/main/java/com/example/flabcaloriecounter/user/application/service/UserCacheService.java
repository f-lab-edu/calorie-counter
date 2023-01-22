package com.example.flabcaloriecounter.user.application.service;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCacheService {
	private final RedisTemplate<String, User> userRedisTemplate;
	private static final Duration USER_TIMEOUT = Duration.ofDays(3);

	public void setUser(final User user) {
		userRedisTemplate.opsForValue().set(getKey(user.userId()), user, USER_TIMEOUT);
	}

	public Optional<User> getUser(final String userId) {
		return Optional.ofNullable(userRedisTemplate.opsForValue().get(getKey(userId)));
	}

	public void deleteAll() {
		userRedisTemplate.delete(Objects.requireNonNull(userRedisTemplate.keys("*")));
	}

	private String getKey(final String userId) {
		return "userId: " + userId;
	}
}

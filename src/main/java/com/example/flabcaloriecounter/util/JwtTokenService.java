package com.example.flabcaloriecounter.util;

import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.exception.UnauthorizedTokenException;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseToken;
import com.example.flabcaloriecounter.user.application.service.RedisTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenService {

	private static final String CLAIM_CONTENT = "userId";
	private final RedisTokenService redisTokenService;
	private final String secretKey;
	private final long accessTokenExpiredTime;
	private final long refreshTokenExpiredTime;

	public JwtTokenService(@Value("${jwt.secret-key}") final String secretKey,
		@Value("${jwt.access-token-expired-ms}") final long accessTokenExpiredTime,
		@Value("${jwt.refresh-token-expired-ms}") final long refreshTokenExpiredTime,
		final RedisTokenService redisTokenService) {
		this.secretKey = secretKey;
		this.accessTokenExpiredTime = accessTokenExpiredTime;
		this.refreshTokenExpiredTime = refreshTokenExpiredTime;
		this.redisTokenService = redisTokenService;
	}

	public ResponseToken generate(final String userId) {
		if (redisTokenService.getRefreshToken(userId).isEmpty()) {
			final String refreshToken = refreshToken(userId);
			redisTokenService.setToken(userId, refreshToken);
			return new ResponseToken(accessToken(userId), refreshToken);
		}

		return new ResponseToken(accessToken(userId), "재발급 하지않음");
	}

	private String accessToken(final String userId) {
		Claims claims = Jwts.claims();
		claims.put(CLAIM_CONTENT, userId);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + this.accessTokenExpiredTime))
			// .setExpiration(new Date(System.currentTimeMillis() + 10000))
			.signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.secretKey)))
			.compact();
	}

	private String refreshToken(final String userId) {
		Claims claims = Jwts.claims();
		claims.put(CLAIM_CONTENT, userId);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + this.refreshTokenExpiredTime))
			// .setExpiration(new Date(System.currentTimeMillis() + 10000))
			.signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.secretKey)))
			.compact();
	}

	public Claims parseClaims(final String jws, final String message) {
		final Jws<Claims> claims;
		try {
			claims = Jwts.parserBuilder()
				.setSigningKey(decodeBase64(this.secretKey))
				.build()
				.parseClaimsJws(jws);

			log.info(">>>>{}", claims);

		} catch (ExpiredJwtException e) {
			throw new UnauthorizedTokenException(message, e);
		} catch (JwtException e) {
			throw new UnauthorizedTokenException("토큰 복호화에 실패했습니다.", e);
		}

		return claims.getBody();
	}
}

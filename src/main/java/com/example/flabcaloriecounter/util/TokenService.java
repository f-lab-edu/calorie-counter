package com.example.flabcaloriecounter.util;

import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.user.application.port.in.dto.ResponseIssuedToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenService {

	private static final String CLAIM_CONTENT = "userId";
	private static final String TOKEN_TYPE = "Bearer";
	private final String secretKey;
	private final long accessTokenExpiredTime;

	public TokenService(@Value("${jwt.secret-key}") final String secretKey,
		@Value("${jwt.access-token-expired-ms}") final long accessTokenExpiredTime
	) {
		this.secretKey = secretKey;
		this.accessTokenExpiredTime = accessTokenExpiredTime;
	}

	public ResponseIssuedToken issue(final String userId) {
		final Claims claims = Jwts.claims();
		claims.put(CLAIM_CONTENT, userId);

		final String accessToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + this.accessTokenExpiredTime))
			.signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.secretKey)))
			.compact();

		return new ResponseIssuedToken(accessToken, TOKEN_TYPE, this.accessTokenExpiredTime);
	}

	public Claims parseClaims(final String jws) {
		final Jws<Claims> claims;
		try {
			claims = Jwts.parserBuilder()
				.setSigningKey(decodeBase64(this.secretKey))
				.build()
				.parseClaimsJws(jws);

			log.info(">>>>{}", claims);

		} catch (ExpiredJwtException e) {
			throw new CustomException(StatusEnum.INVALID_TOKEN, e);
		} catch (JwtException e) {
			throw new CustomException(StatusEnum.FAIL_DECRYPTION, e);
		}

		return claims.getBody();
	}
}

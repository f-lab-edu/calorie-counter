package com.example.flabcaloriecountergateway.security.jwt;

import com.example.flabcaloriecountergateway.security.SecurityConfigProperties.JwtConfigure.AccessTokenProperties;
import com.example.flabcaloriecountergateway.security.SecurityConfigProperties.JwtConfigure.RefreshTokenProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Jwt {
	private final String issuer;
	private final AccessTokenProperties accessTokenProperties;
	private final RefreshTokenProperties refreshTokenProperties;
	private final Algorithm algorithm;
	private final JWTVerifier jwtVerifier;

	public Jwt(String issuer, String clientSecret, AccessTokenProperties accessTokenProperties,
		RefreshTokenProperties refreshTokenProperties) {
		this.issuer = issuer;
		this.algorithm = Algorithm.HMAC512(clientSecret);
		this.accessTokenProperties = accessTokenProperties;
		this.refreshTokenProperties = refreshTokenProperties;
		this.jwtVerifier = JWT.require(algorithm)
			.withIssuer(this.issuer)
			.build();
	}

	public String generateAccessToken(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = JWT.create();

		builder.withSubject(claims.memberId.toString());
		builder.withIssuer(this.issuer);
		builder.withIssuedAt(now);

		if (accessTokenProperties.expirySeconds() > 0) {
			builder.withExpiresAt(new Date(now.getTime() + accessTokenProperties.expirySeconds() * 1000L));
		}
		builder.withClaim("memberId", claims.memberId);
		builder.withClaim("username", claims.username);
		builder.withArrayClaim("roles", claims.roles);

		return builder.sign(algorithm);
	}

	public String generateRefreshToken() {
		Date now = new Date();
		JWTCreator.Builder builder = JWT.create();
		builder.withIssuer(this.issuer);
		builder.withIssuedAt(now);
		if (refreshTokenProperties.expirySeconds() > 0) {
			builder.withExpiresAt(new Date(now.getTime() + refreshTokenProperties.expirySeconds() * 1000L));
		}

		return builder.sign(this.algorithm);
	}

	public Claims decode(String token) {
		return new Claims(JWT.decode(token));

	}

	public Claims verify(String token) {
		return new Claims(this.jwtVerifier.verify(token));

	}

	public List<GrantedAuthority> getAuthorities(Claims claims) {
		String[] roles = claims.roles;

		return roles == null || roles.length == 0
			? Collections.emptyList()
			: Arrays.stream(roles)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	public AccessTokenProperties accessTokenProperties() {
		return accessTokenProperties;
	}

	public RefreshTokenProperties refreshTokenProperties() {
		return refreshTokenProperties;
	}

	public static class Claims {
		Long memberId;
		String username;
		String[] roles;

		Date issuedAt;
		Date expiresAt;

		private Claims() {}

		Claims(DecodedJWT decode) {
			this.memberId = decode.getClaim("memberId").asLong();
			this.username = decode.getClaim("username").asString();
			this.roles = decode.getClaim("roles").asArray(String.class);
			this.issuedAt = decode.getIssuedAt();
			this.expiresAt = decode.getExpiresAt();
		}
	}
}

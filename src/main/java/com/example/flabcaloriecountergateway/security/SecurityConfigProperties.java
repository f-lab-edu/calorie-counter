package com.example.flabcaloriecountergateway.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "security")
public record SecurityConfigProperties(
		PatternsConfigures patterns,
		JwtConfigure jwt
) {

	public SecurityConfigProperties(PatternsConfigures patterns, JwtConfigure jwt) {
		this.patterns = patterns;
		this.jwt = jwt;
	}

	public record PatternsConfigures(Map<String, String[]> ignoring, Map<String, String[]> permitAll) {
	}

	public record JwtConfigure(
		AccessTokenProperties accessToken,
		RefreshTokenProperties refreshToken,
		String issuer,
		String clientSecret
	) {
		public JwtConfigure(
			AccessTokenProperties accessToken,
			RefreshTokenProperties refreshToken,
			String issuer,
			String clientSecret
		) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
			this.issuer = issuer;
			this.clientSecret = clientSecret;
		}

		public record AccessTokenProperties(String header, int expirySeconds) {

		}

		public record RefreshTokenProperties(String header, int expirySeconds) {

		}
	}
}

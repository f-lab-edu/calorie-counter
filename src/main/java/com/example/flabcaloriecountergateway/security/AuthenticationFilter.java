package com.example.flabcaloriecountergateway.security;

import com.example.flabcaloriecountergateway.auth.RequestLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {
		try {
			RequestLoginDto creds = new ObjectMapper()
					.readValue(request.getInputStream(), RequestLoginDto.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					creds.id(),
					creds.password(),
					Collections.emptyList()
			);
			return getAuthenticationManager().authenticate(authToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response, FilterChain chain, Authentication authResult
	) throws IOException, ServletException {
		// TODO: 실제 로그인 성공 시 토큰 제작 (만료시간 등), 응답 객체 등 구체화
	}
}

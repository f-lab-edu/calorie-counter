package com.example.flabcaloriecounter.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.flabcaloriecounter.exception.ReIssuedTokenException;
import com.example.flabcaloriecounter.exception.UnauthorizedTokenException;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.JwtTokenService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

	private static final String TOKEN_CLAIM = "userId";
	private final JwtTokenService jwtTokenService;
	private final UserService userService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(UserAuthentication.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		final String jws = webRequest.getHeader("Authorization");
		if (jws == null || jws.equals("")) {
			throw new UnauthorizedTokenException("요청헤더에 토큰이 없습니다.");
		}

		final String[] splitHeaders = jws.split(",");

		if (splitHeaders.length == 1) {
			final Claims accessClaim = this.jwtTokenService.parseClaims(splitHeaders[0],
				"access 토큰이 만료되었습니다. refresh token과 access token을 같이 보내주세요");

			final User user = this.userService.findByUserId(accessClaim.get(TOKEN_CLAIM).toString())
				.orElseThrow(() -> new UnauthorizedTokenException("access token 헤더의 유저정보가 유효하지않습니다."));

			return new UserAuthentication(user.id(), user.userId());
		}

		if (splitHeaders.length == 2) {
			final Claims refreshClaim = this.jwtTokenService.parseClaims(splitHeaders[1],
				"refresh 토큰이 만료되었습니다. 다시 로그인 해주세요");

			this.userService.findByUserId(refreshClaim.get(TOKEN_CLAIM).toString())
				.orElseThrow(() -> new UnauthorizedTokenException("refresh token 헤더의 유저정보가 유효하지않습니다."));

			throw new ReIssuedTokenException("access token을 재발급했습니다. 다시 로그인해주세요.",
				this.jwtTokenService.generate(refreshClaim.get(TOKEN_CLAIM).toString()));
		}
		throw new UnauthorizedTokenException("요청 헤더 형식이 잘못되었습니다.");
	}
}
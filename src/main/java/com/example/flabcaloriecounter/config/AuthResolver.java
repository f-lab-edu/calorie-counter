package com.example.flabcaloriecounter.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.user.domain.User;
import com.example.flabcaloriecounter.util.StatusEnum;
import com.example.flabcaloriecounter.util.TokenService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

	private static final String TOKEN_CLAIM = "userId";
	private final TokenService tokenService;
	private final UserService userService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(UserAuthentication.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		//요청 헤더 비어있는경우
		final String jws = webRequest.getHeader("Authorization");
		if (jws == null || "".equals(jws)) {
			throw new CustomException(StatusEnum.EMPTY_TOKEN);
		}

		final String[] splitHeaders = jws.split(" ");

		//<Bearer> <token> 형식이 아닌경우
		if (splitHeaders.length != 2) {
			throw new CustomException(StatusEnum.INVALID_TOKEN_FORM);
		}

		//토큰 까봐서 만료,조작여부 확인
		final Claims accessClaim = this.tokenService.parseClaims(splitHeaders[1]);

		//토큰에심은 UserId값이 유효한지 확인
		final User user = this.userService.findByUserId(accessClaim.get(TOKEN_CLAIM).toString())
			.orElseThrow(() -> new CustomException(StatusEnum.INVALID_TOKEN_CONTENTS));

		return new UserAuthentication(user.id(), user.userId());
	}
}
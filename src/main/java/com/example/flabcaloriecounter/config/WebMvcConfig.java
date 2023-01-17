package com.example.flabcaloriecounter.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.util.JwtTokenService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final JwtTokenService jwtTokenService;
	private final UserService userService;

	public WebMvcConfig(final JwtTokenService jwtTokenService, final UserService userService) {
		this.jwtTokenService = jwtTokenService;
		this.userService = userService;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthResolver(this.jwtTokenService, this.userService));
	}
}

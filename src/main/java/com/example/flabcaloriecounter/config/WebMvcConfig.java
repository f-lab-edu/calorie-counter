package com.example.flabcaloriecounter.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.flabcaloriecounter.user.application.service.UserService;
import com.example.flabcaloriecounter.util.TokenService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final TokenService tokenService;
	private final UserService userService;

	public WebMvcConfig(final TokenService tokenService, final UserService userService) {
		this.tokenService = tokenService;
		this.userService = userService;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthResolver(this.tokenService, this.userService));
	}
}

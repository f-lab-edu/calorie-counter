package com.example.flabcaloriecountergateway.security;

import com.example.flabcaloriecountergateway.security.jwt.Jwt;
import com.example.flabcaloriecountergateway.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties({SecurityConfigProperties.class})
public class WebSecurityConfigure {
	private final SecurityConfigProperties securityConfigProperties;

	public WebSecurityConfigure(SecurityConfigProperties securityConfigProperties) {
		this.securityConfigProperties = securityConfigProperties;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Jwt jwt() {
		return new Jwt(
			this.securityConfigProperties.jwt().issuer(),
			this.securityConfigProperties.jwt().clientSecret(),
			this.securityConfigProperties.jwt().accessToken(),
			this.securityConfigProperties.jwt().refreshToken()
		);
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter(Jwt jwt, TokenService tokenService) {
		return new JwtAuthenticationFilter(jwt, tokenService);
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, e) -> {
			response.sendRedirect("/users/signin");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		};
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, e) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.sendRedirect("/users/signin");
		};
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(HttpMethod.GET, this.securityConfigProperties.patterns().ignoring().get("GET"))
			.requestMatchers(HttpMethod.POST, this.securityConfigProperties.patterns().ignoring().get("POST"))
			.requestMatchers(HttpMethod.PATCH, this.securityConfigProperties.patterns().ignoring().get("PATCH"))
			.requestMatchers(HttpMethod.DELETE, this.securityConfigProperties.patterns().ignoring().get("DELETE"))
			.requestMatchers(HttpMethod.PUT, this.securityConfigProperties.patterns().ignoring().get("PUT"))
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, Jwt jwt, TokenService tokenService) throws
		Exception {
		http.authorizeHttpRequests()
			.requestMatchers(HttpMethod.GET, this.securityConfigProperties.patterns().permitAll().get("GET")).permitAll()
			.requestMatchers(HttpMethod.POST, this.securityConfigProperties.patterns().permitAll().get("POST")).permitAll()
			.requestMatchers(HttpMethod.PATCH, this.securityConfigProperties.patterns().permitAll().get("PATCH")).permitAll()
			.requestMatchers(HttpMethod.DELETE, this.securityConfigProperties.patterns().permitAll().get("DELETE")).permitAll()
			.requestMatchers(HttpMethod.PUT, this.securityConfigProperties.patterns().permitAll().get("PUT")).permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().disable()
			.csrf().disable()
			.headers().disable()
			.httpBasic().disable()
			.rememberMe().disable()
			.logout().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.authenticationEntryPoint(authenticationEntryPoint())
			.and()
			.addFilterBefore(jwtAuthenticationFilter(jwt, tokenService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}

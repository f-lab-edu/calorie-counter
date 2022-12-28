package com.example.flabcaloriecountergateway.security.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.flabcaloriecountergateway.exception.JwtTokenNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final Jwt jwt;
	private final TokenService tokenService;
	private final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

	public JwtAuthenticationFilter(Jwt jwt, TokenService tokenService) {
		this.jwt = jwt;
		this.tokenService = tokenService;
	}

	private void logJwtRequest(HttpServletRequest request) {
		logger.log(Level.INFO, "JwtAuthenticationFilter: {0} {1} {2}", new Object[] {
			request.getMethod(),
			request.getRequestURI().toLowerCase(),
			request.getQueryString() == null ? "" : request.getQueryString()
		});
	}

	private JwtAuthenticationToken createAuthenticationToken(Jwt.Claims claims, HttpServletRequest request,
		String accessToken) {
		List<GrantedAuthority> authorities = this.jwt.getAuthorities(claims);
		if (claims.memberId != null && !authorities.isEmpty()) {
			JwtAuthentication authentication = new JwtAuthentication(accessToken, claims.memberId, claims.username);
			JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authentication, null, authorities);
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			return authenticationToken;
		} else {
			throw new JWTDecodeException("Decode Error");
		}
	}

	private String getAccessToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(this.jwt.accessTokenProperties().header()))
				.findFirst()
				.map(Cookie::getValue)
				.orElseThrow(() -> new JwtTokenNotFoundException("AccessToken is not found"));
		} else {
			throw new JwtTokenNotFoundException("AccessToken is not found.");
		}
	}

	private boolean isValidRefreshToken(String refreshToken, String accessToken) {
		// TODO: refresh token 검증 로직 추가
		return true;
	}

	private String getRefreshToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(this.jwt.refreshTokenProperties().header()))
				.findFirst()
				.map(Cookie::getValue)
				.orElseThrow(() -> new JwtTokenNotFoundException("RefreshToken is not found"));
		} else {
			throw new JwtTokenNotFoundException();
		}
	}

	private String accessTokenReIssue(String accessToken) {
		return jwt.generateAccessToken(this.jwt.decode(accessToken));
	}

	private void refreshAuthentication(String accessToken, HttpServletRequest request, HttpServletResponse response) {
		try {
			String refreshToken = getRefreshToken(request);
			if (isValidRefreshToken(refreshToken, accessToken)) {
				String reIssuedAccessToken = accessTokenReIssue(accessToken);
				Jwt.Claims reIssuedClaims = jwt.verify(reIssuedAccessToken);
				JwtAuthenticationToken authentication = createAuthenticationToken(reIssuedClaims, request,
					reIssuedAccessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				Cookie cookie = new Cookie(this.jwt.accessTokenProperties().header(), reIssuedAccessToken);
				cookie.setHttpOnly(true);
				cookie.setPath("/");
				cookie.setMaxAge(this.jwt.accessTokenProperties().expirySeconds());
				response.addCookie(cookie);
			} else {
				logger.warning("refreshToken expired");
			}
		} catch (JwtTokenNotFoundException | JWTVerificationException e) {
			logger.warning(e.getMessage());
		}
	}

	private void authenticate(String accessToken, HttpServletRequest request, HttpServletResponse response) {
		try {
			Jwt.Claims claims = jwt.verify(accessToken);
			JwtAuthenticationToken authToken = createAuthenticationToken(claims, request, accessToken);
			SecurityContextHolder.getContext().setAuthentication(authToken);
			logger.log(Level.INFO, "JwtAuthentication Set");
		} catch (TokenExpiredException e) {
			Cookie cookie = new Cookie(jwt.accessTokenProperties().header(), "");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			logger.warning("JwtAuthentication Expired");

			refreshAuthentication(accessToken, request, response);
		} catch (JWTVerificationException e) {
			logger.warning("JwtAuthentication Invalid");
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logJwtRequest(request);

		try {
			authenticate(getAccessToken(request), request, response);
		} catch (JwtTokenNotFoundException e) {
			logger.log(Level.WARNING, "JwtAuthenticationFilter: {0}", e.getMessage());
		}

		filterChain.doFilter(request, response);
	}
}

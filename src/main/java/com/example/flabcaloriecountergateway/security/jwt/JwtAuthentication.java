package com.example.flabcaloriecountergateway.security.jwt;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

public record JwtAuthentication(String token, Long id, String username) {
	public JwtAuthentication {
		checkArgument(isNotEmpty(token), "access token must be provided");
		checkArgument(id != null, "id must be provided");
		checkArgument(isNotBlank(username), "username must be provided");
	}

}

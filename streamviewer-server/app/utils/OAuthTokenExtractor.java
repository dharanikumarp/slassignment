package utils;

import java.util.Optional;

import play.mvc.Http.Request;

public final class OAuthTokenExtractor {

	public static final String getOAuthToken(final Request req) {
		String oauthToken = null;
		Optional<String> authorizationHeader = req.header("Authorization");

		if (authorizationHeader.isPresent()) {
			oauthToken = authorizationHeader.get().substring(7);
		}
		return oauthToken;
	}
}

package action;

import static utils.IdTokenVerifier.verifyIdToken;

import java.util.Optional;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import play.mvc.Http.Context;
import play.mvc.Security.Authenticator;

public class AccessTokenVerifierAction extends Authenticator {

	@Override
	public String getUsername(Context ctx) {
		String email = null;
		Optional<String> authorizationHeader = ctx.request().header("Authorization");

		if (authorizationHeader.isPresent()) {
			String idTokenStr = authorizationHeader.get().substring(7);

			GoogleIdToken idToken = verifyIdToken(idTokenStr);
			email = idToken != null ? idToken.getPayload().getEmail() : null;
		}
		return email;
	}
}

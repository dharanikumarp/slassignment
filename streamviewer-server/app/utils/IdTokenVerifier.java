package utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public final class IdTokenVerifier {

	private static final String CLIENT_ID = "450677404358-luoq4fsgakvfq482n0j3fbhtd93ne0ve.apps.googleusercontent.com";

	public static GoogleIdToken verifyIdToken(final String idTokenStr) {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance()).setAudience(Collections.singletonList(CLIENT_ID)).build();

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(idTokenStr);

		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		return idToken;
	}
}

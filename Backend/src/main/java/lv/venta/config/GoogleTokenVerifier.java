package lv.venta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenVerifier {

	private JwtDecoder googleJwtDecoder;
	private String expectedClientId;

	public GoogleTokenVerifier(
			@Value("${spring.security.oauth2.client.registration.google.client-id}") String expectedClientId) {
		this.expectedClientId = expectedClientId;
		this.googleJwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
	}

	public Jwt verify(String token) {
		Jwt jwt = googleJwtDecoder.decode(token);

		if (!jwt.getIssuer().toString().equals("accounts.google.com")
				&& !jwt.getIssuer().toString().equals("https://accounts.google.com")) {
			throw new JwtException("Invalid issuer");
		}

		if (!jwt.getAudience().contains(expectedClientId)) {
			throw new JwtException("Invalid audience");
		}
		return jwt;
	}
}

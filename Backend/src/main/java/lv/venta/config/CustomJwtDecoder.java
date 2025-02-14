package lv.venta.config;

import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder implements JwtDecoder {

	private JwtDecoder googleJwtDecoder;
	private JwtDecoder customJwtDecoder;

	public CustomJwtDecoder(@Value("${jwt.secret}") String secretKey) {
		this.googleJwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();

		byte[] decodedKey = Base64.getDecoder().decode(secretKey);
		SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "HmacSHA256");

		this.customJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
	}

	@Override
	public Jwt decode(String token) throws JwtException {
		try {
			Jwt jwt = googleJwtDecoder.decode(token);
			String issuer = jwt.getClaimAsString("iss");

			if (issuer != null && issuer.contains("accounts.google.com")) {
				return jwt;
			}
		} catch (Exception ignored) {
		}
		return customJwtDecoder.decode(token);
	}
}

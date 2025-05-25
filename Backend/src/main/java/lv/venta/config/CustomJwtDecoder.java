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

	private JwtDecoder jwtDecoder;

	public CustomJwtDecoder(@Value("${jwt.secret}") String secretKey) {

		byte[] decodedKey = Base64.getDecoder().decode(secretKey);
		SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "HmacSHA256");

		this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
	}

	@Override
	public Jwt decode(String token) throws JwtException {
		return jwtDecoder.decode(token);
	}
}

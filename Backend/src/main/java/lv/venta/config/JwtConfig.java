package lv.venta.config;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Configuration
public class JwtConfig {

	@Value("${jwt.secret}")
	private String secretKey;

	public JwtEncoder jwtEncoder() {
		return parameters -> {
			byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

			try {
				MACSigner signer = new MACSigner(secretKeySpec);

				JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
				parameters.getClaims().getClaims().forEach((key, value) -> claimsSetBuilder.claim(key,
						value instanceof Instant ? Date.from((Instant) value) : value));
				JWTClaimsSet claimsSet = claimsSetBuilder.build();

				JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

				SignedJWT signedJWT = new SignedJWT(header, claimsSet);
				signedJWT.sign(signer);

				return Jwt.withTokenValue(signedJWT.serialize()).header("alg", header.getAlgorithm().getName())
						.subject(claimsSet.getSubject()).issuer(claimsSet.getIssuer())
						.claims(claims -> claims.putAll(claimsSet.getClaims()))
						.issuedAt(claimsSet.getIssueTime().toInstant())
						.expiresAt(claimsSet.getExpirationTime().toInstant()).build();
			} catch (Exception e) {
				throw new IllegalStateException("Error while signing the JWT", e);
			}
		};
	}

	public String generateToken(String email, String role, String name) {
		if (secretKey == null || secretKey.isEmpty()) {
			throw new IllegalStateException("JWT secret key is not set properly");
		}

		Instant now = Instant.now();

		JwtClaimsSet claims = JwtClaimsSet.builder().subject(email).claim("role", role).claim("name", name)
				.issuedAt(now).expiresAt(now.plusSeconds(3600)).build();

		return jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
	
	public String generateRefreshToken(String email) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
			.subject(email)
			.issuedAt(now)
			.expiresAt(now.plusSeconds(604800)) // 7 days
			.build();

		return jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}

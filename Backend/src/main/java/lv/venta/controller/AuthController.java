package lv.venta.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

	private final JwtDecoder jwtDecoder;

	public AuthController(JwtDecoder jwtDecoder) {
		this.jwtDecoder = jwtDecoder;
	}

	@GetMapping("/user")
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Invalid or missing Authorization header");
		}
		String token = authorizationHeader.substring(7);
		try {
			Jwt jwt = jwtDecoder.decode(token);

			Authentication authentication;
			Collection<GrantedAuthority> authorities = extractAuthoritiesFromClaims(jwt.getClaims());

			if (jwt.getClaims().containsKey("sub")) {
				authentication = new JwtAuthenticationToken(jwt, authorities);
			} else {
				return ResponseEntity.status(401).body("Invalid JWT structure");
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);

			Map<String, Object> userDetails = new HashMap<>();
			userDetails.put("name", jwt.getClaimAsString("name"));
			userDetails.put("email", jwt.getClaimAsString("email"));

			return ResponseEntity.ok(userDetails);
		} catch (Exception e) {
			System.err.println("JWT decoding failed: " + e.getMessage());
			return ResponseEntity.status(401).body("Invalid or expired token");
		}
	}

	private Collection<GrantedAuthority> extractAuthoritiesFromClaims(Map<String, Object> claims) {
		Object authoritiesClaim = claims.getOrDefault("authorities", claims.getOrDefault("roles", new ArrayList<>()));

		if (authoritiesClaim instanceof Collection<?>) {
			return ((Collection<?>) authoritiesClaim).stream().filter(String.class::isInstance)
					.map(authority -> new SimpleGrantedAuthority("ROLE_" + authority)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

//	@PostMapping("/logout")
//	public ResponseEntity<?> logout() {
//		SecurityContextHolder.clearContext();
//		return ResponseEntity.ok().build();
//	}
}

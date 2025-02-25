package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lv.venta.config.CustomJwtDecoder;
import lv.venta.config.JwtConfig;
import lv.venta.model.Role;
import lv.venta.model.User;
import lv.venta.service.IUserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

	private JwtConfig jwtConfig;
	private CustomJwtDecoder jwtDecoder;

	public AuthController(JwtConfig jwtConfig, CustomJwtDecoder jwtDecoder) {
		this.jwtConfig = jwtConfig;
		this.jwtDecoder = jwtDecoder;
	}

	@Autowired
	private IUserService userService;

	@GetMapping("/user")
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Invalid or missing Authorization header");
		}
		String token = authorizationHeader.substring(7);
		try {
			Jwt jwt = jwtDecoder.decode(token);

			Authentication authentication;

			if (jwt.getClaims().containsKey("sub")) {
				authentication = new JwtAuthenticationToken(jwt);
			} else {
				return ResponseEntity.status(401).body("Invalid JWT structure");
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String email = jwt.getClaimAsString("email");
			String name = jwt.getClaimAsString("name");
			String picture = jwt.getClaimAsString("picture");

			User user = null;
			try {
				user = userService.selectByEmail(email);
			} catch (Exception e) {
			}
			if (user == null) {
				if (!email.split("@")[1].equals("venta.lv")) {
					return new ResponseEntity<String>("You can only use a venta.lv email to login",
							HttpStatus.INTERNAL_SERVER_ERROR);
				} else {
					user = new User(name, email, Role.USER);
					userService.insertNewUser(user);
				}
			}

			String role = user.getRole().toString();

			String newToken = jwtConfig.generateToken(email, role, name, picture);
			return new ResponseEntity<String>(newToken, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("JWT decoding failed: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

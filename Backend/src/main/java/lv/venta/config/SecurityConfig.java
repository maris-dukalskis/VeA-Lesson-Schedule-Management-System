package lv.venta.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomJwtDecoder customJwtDecoder;

	public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
		this.customJwtDecoder = customJwtDecoder;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/classroom/**").authenticated()
						.requestMatchers("/course/**").authenticated()
						.requestMatchers("/lecturer/**").authenticated()
						.requestMatchers("/student/**").authenticated()
						.requestMatchers("/user/**").authenticated()
						
						.requestMatchers("/lesson/all").permitAll()
						.requestMatchers("/lesson/studyprogramme/**").permitAll()
						.requestMatchers("/lesson/get/**").authenticated()
						.requestMatchers("/lesson/update/**").authenticated()
						.requestMatchers("/lesson/insert").authenticated()
						.requestMatchers("/lesson/delete/**").authenticated()
						
						.requestMatchers("/lessondatetime/all").authenticated()
						.requestMatchers("/lessondatetime/get/**").authenticated()
						.requestMatchers("/lessondatetime/update/**").authenticated()
						.requestMatchers("/lessondatetime/insert").authenticated()
						.requestMatchers("/lessondatetime/delete/**").authenticated()
						.requestMatchers("/lessondatetime/lesson/**").permitAll()
						
						.requestMatchers("/studyprogramme/all").permitAll()
						.requestMatchers("/studyprogramme/get/**").authenticated()
						.requestMatchers("/studyprogramme/insert").authenticated()
						.requestMatchers("/studyprogramme/update/**").authenticated()
						.requestMatchers("/studyprogramme/delete/**").authenticated())
				.oauth2Login(Customizer.withDefaults())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(customJwtDecoder))
						.authenticationEntryPoint((request, response, authException) -> {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.getWriter().write("Token expired or invalid, please log in again");
						}))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
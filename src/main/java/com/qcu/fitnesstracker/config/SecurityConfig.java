package com.qcu.fitnesstracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/api/**")
				)

				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/google-login", "/oauth2/**", "/auth/logout").permitAll()
						.requestMatchers("/api/workouts/nearby", "/api/register","/api/login", "/auth/success", "/api/status",
								 "/api/status",		"/api/status/**").permitAll()
						.requestMatchers("/oauth2/authorization/google").permitAll()

						.requestMatchers("/auth/success").permitAll()

						.requestMatchers("/oauth/success/").permitAll()

						.requestMatchers("/api/nutrition/**").permitAll()
						.requestMatchers("/api/meals/**").permitAll()
						.requestMatchers("/api/meals/suggest/**").permitAll()

						.requestMatchers("/api/fitbit/steps").permitAll()
						.requestMatchers("/api/fitbit/callback").permitAll()
						.requestMatchers("/api/fitbit/energy").permitAll()

						.requestMatchers("/api/user/**").permitAll()
						.requestMatchers("/api/user").permitAll()


						.requestMatchers("/api/fitbit/**").permitAll()
						.requestMatchers("/api/oauth/**").permitAll()



						// ✅ Allow ALL Strava endpoints without authentication
						.requestMatchers("/api/strava/**").permitAll()
						.requestMatchers("/api/strava/login", "/api/strava/callback", "/api/strava/token").permitAll() // Allow Strava callback and token retrieval without Google login

						.requestMatchers("/boink/test/**").permitAll()

						// 🚨 Ensure other /api/** routes are protected AFTER defining the Strava rules
						.requestMatchers("/api/**").authenticated()

						.anyRequest().authenticated()
				)
				.oauth2Login(oauth -> oauth
						.loginPage("/auth/google-login")
						.defaultSuccessUrl("/auth/success", true)

				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
						.addLogoutHandler((request, response, authentication) -> {
							if (authentication != null) {
								SecurityContextHolder.clearContext();
							}
							request.getSession().invalidate();
						})
						.logoutSuccessUrl("/auth/google-login")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				);

		return http.build();
	}

	@Bean
	public HttpFirewall allowSemicolonHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowSemicolon(true); // ✅ Allow semicolons in URLs
		return firewall;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
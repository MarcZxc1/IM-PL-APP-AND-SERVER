package com.qcu.fitnesstracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/google-login", "/oauth2/**", "/auth/logout").permitAll()
						.requestMatchers("/api/workouts/nearby", "/auth/success").permitAll()

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
}
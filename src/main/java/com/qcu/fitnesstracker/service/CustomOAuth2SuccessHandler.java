package com.qcu.fitnesstracker.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final HttpSession session;
	private final AuthTokenStore authTokenStore;

	public CustomOAuth2SuccessHandler(HttpSession session, AuthTokenStore authTokenStore) {
		this.session = session;
		this.authTokenStore = authTokenStore;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication) throws IOException {

		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		OAuth2User user = token.getPrincipal();

		// Generate a unique token
		String authToken = UUID.randomUUID().toString();
		
		// Store user info in session
		session.setAttribute("oauthUser", user.getAttributes());
		session.setAttribute("AUTH_TOKEN", authToken);
		session.setAttribute("AUTHENTICATED", true);
		
		// Store token in AuthTokenStore
		authTokenStore.storeToken(authToken, user.getAttribute("email"));

		// Redirect to the success page with the token
		response.sendRedirect("/auth/success?token=" + authToken);
	}
}

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

		// Store user info in session
		session.setAttribute("AUTHENTICATED", true);
		session.setAttribute("USER_EMAIL", user.getAttribute("email"));
		session.setAttribute("USER_NAME", user.getAttribute("name"));
		session.setAttribute("USER_PICTURE", user.getAttribute("picture"));

		// Redirect to success page
		response.sendRedirect("/auth/success");
	}
}

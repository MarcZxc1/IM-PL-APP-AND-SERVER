package com.qcu.fitnesstracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GoogleAuthController {

	@GetMapping("/auth/google-login")
	public RedirectView redirectToGoogle(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (session != null) {
			session.invalidate(); // Ensure session is cleared
		}

		// Clear any existing authentication
		SecurityContextHolder.clearContext();

		// Redirect to Google OAuth2 login every time
		return new RedirectView("/oauth2/authorization/google");
	}
}

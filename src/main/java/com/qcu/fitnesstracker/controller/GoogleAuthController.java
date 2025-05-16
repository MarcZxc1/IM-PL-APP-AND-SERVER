package com.qcu.fitnesstracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GoogleAuthController {

	@GetMapping("/auth/google-login")
	public RedirectView redirectToGoogle(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (session != null) {
			session.invalidate(); // Ensure session is cleared
		}

		// Clear any existing authentication
//		SecurityContextHolder.clearContext();

		// Redirect to Google OAuth2 login every time
		return new RedirectView("/oauth2/authorization/google");
	}
//
//	@GetMapping("/api/status")
//	@ResponseBody
//	public ResponseEntity<?> checkAuthStatus(HttpSession session) {
//		System.out.println("[DEBUG] Checking auth status for session: " + session.getId());
//
//		// Check if user is authenticated in the session
//		Boolean authenticated = (Boolean) session.getAttribute("AUTHENTICATED");
//		String email = (String) session.getAttribute("USER_EMAIL");
//		String name = (String) session.getAttribute("USER_NAME");
//		String picture = (String) session.getAttribute("USER_PICTURE");
//
//		System.out.println("[DEBUG] Session attributes - authenticated: " + authenticated +
//						 ", email: " + email +
//						 ", name: " + name);
//
//		if (authenticated != null && authenticated) {
//			Map<String, Object> status = new HashMap<>();
//			status.put("authenticated", true);
//			status.put("email", email);
//			status.put("name", name);
//			status.put("picture", picture);
//			System.out.println("[DEBUG] Returning authenticated status");
//			return ResponseEntity.ok(status);
//		}
//
//		System.out.println("[DEBUG] Returning unauthenticated status");
//		return ResponseEntity.ok(Map.of("authenticated", false));
//	}
}

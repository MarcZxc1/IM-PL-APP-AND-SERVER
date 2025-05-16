package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.service.GoogleOAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OAuthCallbackController {

	private final GoogleOAuthService googleOAuthService;

	@Autowired
	public OAuthCallbackController(GoogleOAuthService googleOAuthService) {
		this.googleOAuthService = googleOAuthService;
	}

	@GetMapping(value = "/login-url", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> getLoginUrl() {
		try {
			String authUrl = googleOAuthService.getAuthorizationUrl();
			Map<String, String> response = new HashMap<>();
			response.put("url", authUrl);
			
			return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Failed to generate login URL: " + e.getMessage());
			return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(error);
		}
	}

	@GetMapping(value = "/callback", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> handleCallback(@RequestParam("code") String code, HttpSession session) {
		try {
			// Exchange code for token
			Map<String, Object> tokenResponse = googleOAuthService.exchangeCodeForToken(code);
			String accessToken = (String) tokenResponse.get("access_token");

			// Get user info from Google
			Map<String, Object> userInfo = googleOAuthService.getUserInfo(accessToken);

			// Save or update user in database
			googleOAuthService.saveOrUpdateUser(userInfo);

			// Store user info in session
			session.setAttribute("AUTHENTICATED", true);
			session.setAttribute("USER_EMAIL", userInfo.get("email"));
			session.setAttribute("USER_NAME", userInfo.get("name"));
			session.setAttribute("USER_PICTURE", userInfo.get("picture"));

			// Return success response
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Authentication successful");
			response.put("user", userInfo);

			return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "Authentication failed: " + e.getMessage());
			return ResponseEntity.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(errorResponse);
		}
	}
}

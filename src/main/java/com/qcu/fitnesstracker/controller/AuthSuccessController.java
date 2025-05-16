package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.model.GoogleUser;
import com.qcu.fitnesstracker.repository.GoogleUserRepository;
import com.qcu.fitnesstracker.service.AuthTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RestController
public class AuthSuccessController {

	@Autowired
	private AuthTokenStore tokenStore;

	@Autowired
	private GoogleUserRepository googleUserRepository;

	private final Map<String, Boolean> loginStatus = new ConcurrentHashMap<>();

	@GetMapping("/auth/success")
	@ResponseBody
	public String loginSuccess(@AuthenticationPrincipal OAuth2User principal, HttpSession session, HttpServletResponse response) {
		if (principal != null) {
			System.out.println("[DEBUG] Processing successful login for user: " + principal.getAttribute("email"));
			
			// Save or update user in MongoDB
			GoogleUser googleUser = googleUserRepository.findByEmail(principal.getAttribute("email"));
			if (googleUser == null) {
				googleUser = new GoogleUser();
				googleUser.setGoogleId(principal.getAttribute("sub"));
				googleUser.setEmail(principal.getAttribute("email"));
				googleUser.setName(principal.getAttribute("name"));
				googleUser.setPicture(principal.getAttribute("picture"));
				googleUserRepository.save(googleUser);
				System.out.println("[DEBUG] Created new Google user in MongoDB");
			} else {
				// Update existing user
				googleUser.setName(principal.getAttribute("name"));
				googleUser.setPicture(principal.getAttribute("picture"));
				googleUserRepository.save(googleUser);
				System.out.println("[DEBUG] Updated existing Google user in MongoDB");
			}
			
			// Store user info in session
			session.setAttribute("AUTHENTICATED", true);
			session.setAttribute("USER_EMAIL", principal.getAttribute("email"));
			session.setAttribute("USER_NAME", principal.getAttribute("name"));
			session.setAttribute("USER_PICTURE", principal.getAttribute("picture"));
			
			// Get the session ID
			String sessionId = session.getId();
			System.out.println("[DEBUG] Session ID: " + sessionId);
			
			// Set the session cookie
			Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
			sessionCookie.setPath("/");
			sessionCookie.setHttpOnly(true);
			sessionCookie.setSecure(true);
			response.addCookie(sessionCookie);
			
			System.out.println("[DEBUG] Session cookie set and attributes stored");
			
			StringBuilder html = new StringBuilder();
			html.append("<!DOCTYPE html>")
				.append("<html>")
				.append("<head>")
				.append("<title>Login Successful</title>")
				.append("<meta charset=\"UTF-8\">")
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
				.append("<style>")
				.append("body { font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; background-color: #f0f2f5; }")
				.append(".container { text-align: center; padding: 2rem; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 400px; width: 90%; }")
				.append("h1 { color: #1a73e8; margin-bottom: 1rem; }")
				.append("p { color: #5f6368; margin-bottom: 2rem; }")
				.append(".spinner { border: 4px solid #f3f3f3; border-top: 4px solid #1a73e8; border-radius: 50%; width: 40px; height: 40px; animation: spin 1s linear infinite; margin: 0 auto; }")
				.append("@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }")
				.append("</style>")
				.append("</head>")
				.append("<body>")
				.append("<div class=\"container\">")
				.append("<h1>Login Successful!</h1>")
				.append("<p>You have successfully logged in. Returning to the application...</p>")
				.append("<div class=\"spinner\"></div>")
				.append("</div>")
				.append("<script>")
				.append("window.opener.postMessage({ type: 'LOGIN_SUCCESS', sessionId: '").append(sessionId).append("' }, '*');")
				.append("setTimeout(() => { window.close(); }, 2000);")
				.append("</script>")
				.append("</body>")
				.append("</html>");
			
			return html.toString();
		}
		System.out.println("[DEBUG] Login failed - no principal found");
		return "Login failed";
	}

	@GetMapping("/oauth/success")
	public RedirectView oauthSuccess(Authentication authentication) {
		String username = authentication.getName();
		String token = UUID.randomUUID().toString();
		tokenStore.storeToken(token, username);
		return new RedirectView("http://localhost:8080/login-complete?token=" + token);
	}
}
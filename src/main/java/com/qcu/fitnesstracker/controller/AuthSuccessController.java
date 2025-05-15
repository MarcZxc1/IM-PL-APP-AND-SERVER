package com.qcu.fitnesstracker.controller;

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

	private final Map<String, Boolean> loginStatus = new ConcurrentHashMap<>();
//
//	@GetMapping("/api/oauth-success")
//	public ResponseEntity<String> onSuccess(OAuth2AuthenticationToken authentication, HttpSession session) {
//		String sessionId = session.getId();
//		loginStatus.put(sessionId, true);
//		return ResponseEntity.ok("Login Successful! You can close the browser.");
//	}
//
//	@GetMapping("/api/status")
//	public Map<String, Object> checkStatus(HttpSession session) {
//		String sessionId = session.getId();
//		boolean authenticated = loginStatus.getOrDefault(sessionId, false);
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("authenticated", authenticated);
//		response.put("name", "Google User");
//		response.put("email", "user@gmail.com");
//		return response;
//	}

	@GetMapping("/auth/success")
	@ResponseBody
	public String loginSuccess(@AuthenticationPrincipal OAuth2User principal, HttpSession session, HttpServletResponse response) {
		if (principal != null) {
			// Store user info in session
			session.setAttribute("LOGGED_IN_USER", principal.getAttribute("sub"));
			session.setAttribute("USER_EMAIL", principal.getAttribute("email"));
			session.setAttribute("USER_NAME", principal.getAttribute("name"));
			session.setAttribute("USER_PICTURE", principal.getAttribute("picture"));
			session.setAttribute("AUTHENTICATED", true);

			System.out.println("User logged in: " + principal.getAttribute("email"));

			return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Login Successful</title>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                            background-color: #f0f2f5;
                        }
                        .container {
                            text-align: center;
                            padding: 2rem;
                            background: white;
                            border-radius: 8px;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            max-width: 400px;
                            width: 90%;
                        }
                        h1 {
                            color: #1a73e8;
                            margin-bottom: 1rem;
                        }
                        p {
                            color: #5f6368;
                            margin-bottom: 2rem;
                        }
                        .spinner {
                            border: 4px solid #f3f3f3;
                            border-top: 4px solid #1a73e8;
                            border-radius: 50%;
                            width: 40px;
                            height: 40px;
                            animation: spin 1s linear infinite;
                            margin: 0 auto;
                        }
                        @keyframes spin {
                            0% { transform: rotate(0deg); }
                            100% { transform: rotate(360deg); }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Login Successful!</h1>
                        <p>You have successfully logged in. Returning to the application...</p>
                        <div class="spinner"></div>
                    </div>
                    <script>
                        // Close the window after 2 seconds
                        setTimeout(() => {
                            window.close();
                        }, 2000);
                    </script>
                </body>
                </html>
                """;
		}
		return "Login failed";
	}

	@GetMapping("/oauth/success")
	public RedirectView oauthSuccess(Authentication authentication) {
		String username = authentication.getName(); // Or get full user info
		String token = UUID.randomUUID().toString();

		tokenStore.storeToken(token, username);

		// Redirect to a static success page or deep link with the token
		return new RedirectView("http://localhost:8080/login-complete?token=" + token);
	}
}
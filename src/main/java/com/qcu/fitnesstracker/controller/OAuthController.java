package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.model.GoogleUser;
import com.qcu.fitnesstracker.repository.GoogleUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OAuthController {

	private final GoogleUserRepository googleUserRepository;

	public OAuthController(GoogleUserRepository googleUserRepository) {
		this.googleUserRepository = googleUserRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
		String email = credentials.get("email");
		String rawPassword = credentials.get("password");

		GoogleUser user = googleUserRepository.findByEmail(email);

		if (user == null) {
			System.err.println("❌ User not found: " + email);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
		}

		// ✅ Compare passwords directly (No hashing)
		if (!rawPassword.equals(user.getPassword())) {
			System.err.println("❌ Password mismatch for user: " + email);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}

		return ResponseEntity.ok("Login successful");
	}

//	@GetMapping("/success")
//	public String loginSuccess(OAuth2AuthenticationToken authentication, HttpServletRequest request) {
//		HttpSession session = request.getSession(true);
//		String googleId = authentication.getPrincipal().getAttribute("sub");
//		String email = authentication.getPrincipal().getAttribute("email");
//		String name = authentication.getPrincipal().getAttribute("name");
//		String picture = authentication.getPrincipal().getAttribute("picture");
//
//		session.setAttribute("LOGGED_IN_USER", googleId);
//		System.out.println("✅ User logged in: " + email);
//
//		GoogleUser existingUser = googleUserRepository.findByGoogleId(googleId);
//
//		if (existingUser == null) {
//			GoogleUser newUser = new GoogleUser(googleId, email, name, picture, null);
//			googleUserRepository.save(newUser);
//		}
//
//		return "<html style='display: flex; justify-content: center; align-items: center; height: 100vh;'>" +
//				"<body><h1 style='text-align: center;'>Login Successful!</h1>" +
//				"<p style='text-align: center;'>You can now close this tab.</p></body></html>";
//	}


	@GetMapping("/api/user")
	public ResponseEntity<?> getUserInfo(HttpSession session) {
		String loggedInUser = (String) session.getAttribute("LOGGED_IN_USER");
		if (loggedInUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is logged in.");
		}
		GoogleUser user = googleUserRepository.findByGoogleId(loggedInUser);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (session != null) {
			session.invalidate();
		}

		SecurityContextHolder.clearContext();

		Cookie cookie = new Cookie("JSESSIONID", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return ResponseEntity.ok("User logged out successfully.");
	}
}
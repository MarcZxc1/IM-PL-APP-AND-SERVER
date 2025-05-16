package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.model.GoogleUser;
import com.qcu.fitnesstracker.model.User;
import com.qcu.fitnesstracker.repository.GoogleUserRepository;
import com.qcu.fitnesstracker.repository.UserRepository;
import com.qcu.fitnesstracker.service.AuthTokenStore;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OAuthController {


	@Autowired
	private AuthTokenStore tokenStore;

	private final GoogleUserRepository googleUserRepository;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;

	public OAuthController(GoogleUserRepository googleUserRepository,
						   UserRepository userRepository,
						   AuthenticationManager authenticationManager,
						   PasswordEncoder passwordEncoder) {
		this.googleUserRepository = googleUserRepository;
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
		try {
			String email = userData.get("email");
			String password = userData.get("password");
			String firstName = userData.get("firstName");
			String lastName = userData.get("lastName");

			if (email == null || password == null || firstName == null || lastName == null) {
				return ResponseEntity.badRequest().body("All fields are required");
			}

			// Check if user already exists in either repository
			if (userRepository.existsByEmail(email) || googleUserRepository.findByEmail(email) != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
			}

			// Create new regular user
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setPassword(passwordEncoder.encode(password));
			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);

			userRepository.save(newUser);

			// Create response map
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("message", "Registration successful");
			responseMap.put("email", email);
			responseMap.put("name", firstName + " " + lastName);

			return ResponseEntity.ok(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred during registration: " + e.getMessage());
		}
	}
	// ... existing code ...
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
		try {
			String email = credentials.get("email");
			String password = credentials.get("password");

			if (email == null || password == null) {
				return ResponseEntity.badRequest().body("Email and password are required");
			}

			// Try to authenticate
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(email, password)
			);

			// Check both repositories for user info
			User regularUser = userRepository.findByEmail(email);
			GoogleUser googleUser = googleUserRepository.findByEmail(email);

			Map<String, Object> userInfo = new HashMap<>();
			if (regularUser != null) {
				// Store user info in session
				session.setAttribute("USER_EMAIL", regularUser.getEmail());
				session.setAttribute("USER_NAME", regularUser.getFirstName() + " " + regularUser.getLastName());

				userInfo.put("id", regularUser.getId());
				userInfo.put("email", regularUser.getEmail());
				userInfo.put("name", regularUser.getFirstName() + " " + regularUser.getLastName());
			} else if (googleUser != null) {
				// Store user info in session
				session.setAttribute("USER_EMAIL", googleUser.getEmail());
				session.setAttribute("USER_NAME", googleUser.getName());
				session.setAttribute("USER_PICTURE", googleUser.getPicture());

				userInfo.put("id", googleUser.getGoogleId());
				userInfo.put("email", googleUser.getEmail());
				userInfo.put("name", googleUser.getName());
				userInfo.put("picture", googleUser.getPicture());
			}

			return ResponseEntity.ok(userInfo);
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred during login: " + e.getMessage());
		}
	}
// ... existing code ...

	@GetMapping("/status")
	public ResponseEntity<?> checkAuthStatus(HttpSession session) {
		try {
			// 1. Check Spring Security context first
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
				return ResponseEntity.ok(Map.of(
						"authenticated", true,
						"email", auth.getName(),
						"source", "security_context"
				));
			}

			// 2. Check session attributes
			Boolean sessionAuthenticated = (Boolean) session.getAttribute("AUTHENTICATED");
			if (sessionAuthenticated != null && sessionAuthenticated) {
				return ResponseEntity.ok(Map.of(
						"authenticated", true,
						"email", session.getAttribute("USER_EMAIL"),
						"source", "session"
				));
			}

			return ResponseEntity.ok(Map.of("authenticated", false));

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error checking status");
		}
	}

	@GetMapping("/oauth/login-url")
	public ResponseEntity<?> getGoogleLoginUrl(HttpServletRequest request) {
		String redirectUrl = "http://localhost:8080/oauth2/authorization/google";
		return ResponseEntity.ok(Map.of("url", redirectUrl));
	}

	// Enhanced user endpoint with session handling
	@GetMapping("/user")
	public ResponseEntity<?> getUserInfo(
			@AuthenticationPrincipal OAuth2User principal,
			HttpSession session) {

		if (principal == null) {
			return ResponseEntity.status(401).body("Not authenticated");
		}

		try {
			String email = principal.getAttribute("email");
			String name = principal.getAttribute("name");
			String picture = principal.getAttribute("picture");

			// Update session with authentication state
			session.setAttribute("AUTHENTICATED", true);
			session.setAttribute("USER_EMAIL", email);
			session.setAttribute("USER_NAME", name);
			session.setAttribute("USER_PICTURE", picture);

			// Save/update user in database
			GoogleUser user = googleUserRepository.findByEmail(email);
			if (user == null) {
				user = new GoogleUser();
				user.setGoogleId(principal.getAttribute("sub"));
				user.setEmail(email);
				user.setName(name);
				user.setPicture(picture);
				googleUserRepository.save(user);
			}

			Map<String, Object> response = new HashMap<>();
			response.put("authenticated", true);
			response.put("email", email);
			response.put("name", name);
			response.put("picture", picture);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error processing user info");
		}
	}

	@GetMapping("/token")
	public Map<String, Object> getToken(
			@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
			OAuth2AuthenticationToken authentication
	) {
		String accessToken = authorizedClient.getAccessToken().getTokenValue();

		return Map.of(
				"accessToken", accessToken,
				"principal", authentication.getPrincipal().getAttributes()
		);
	}

//	@GetMapping("/user")
//	public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User principal, HttpSession session) {
//		if (principal == null) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is logged in.");
//		}
//
//		try {
//			String googleId = principal.getAttribute("sub");
//			String email = principal.getAttribute("email");
//			String name = principal.getAttribute("name");
//			String picture = principal.getAttribute("picture");
//
//			// Store user info in session
//			session.setAttribute("LOGGED_IN_USER", googleId);
//			session.setAttribute("USER_EMAIL", email);
//			session.setAttribute("USER_NAME", name);
//			session.setAttribute("USER_PICTURE", picture);
//
//			// Check if user exists in database
//			GoogleUser existingUser = googleUserRepository.findByEmail(email);
//			if (existingUser == null) {
//				// Create new user if doesn't exist
//				GoogleUser newUser = new GoogleUser();
//				newUser.setGoogleId(googleId);
//				newUser.setEmail(email);
//				newUser.setName(name);
//				newUser.setPicture(picture);
//				googleUserRepository.save(newUser);
//				existingUser = newUser;
//			}
//
//			Map<String, Object> userInfo = new HashMap<>();
//			userInfo.put("id", googleId);
//			userInfo.put("email", email);
//			userInfo.put("name", name);
//			userInfo.put("picture", picture);
//
//			return ResponseEntity.ok(userInfo);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing user info");
//		}
//	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during logout");
		}
	}
}
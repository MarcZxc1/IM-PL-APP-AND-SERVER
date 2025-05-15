//package com.qcu.fitnesstracker.controller;
//
//import com.qcu.fitnesstracker.model.GoogleUser;
//import com.qcu.fitnesstracker.repository.GoogleUserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
//public class OAuth2Controller {
//
//	private final GoogleUserRepository googleUserRepository;
//
//	public OAuth2Controller(GoogleUserRepository googleUserRepository) {
//		this.googleUserRepository = googleUserRepository;
//	}
//
//	@GetMapping("/success")
//	public ResponseEntity<?> handleOAuthSuccess(@AuthenticationPrincipal OAuth2User principal,
//												HttpServletRequest request,
//												HttpServletResponse response,
//												HttpSession session) {
//		try {
//			if (principal == null) {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is logged in.");
//			}
//
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
//			// Create response with user info
//			Map<String, Object> userInfo = new HashMap<>();
//			userInfo.put("id", googleId);
//			userInfo.put("email", email);
//			userInfo.put("name", name);
//			userInfo.put("picture", picture);
//
//			// Set CORS headers
//			response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//			response.setHeader("Access-Control-Allow-Credentials", "true");
//			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//			response.setHeader("Access-Control-Allow-Headers", "*");
//
//			return ResponseEntity.ok(userInfo);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error processing OAuth success: " + e.getMessage());
//		}
//	}
//
//	@GetMapping("/failure")
//	public ResponseEntity<?> handleOAuthFailure() {
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//				.body("OAuth authentication failed");
//	}
//
//	@GetMapping("/check")
//	public ResponseEntity<?> checkAuthStatus(@AuthenticationPrincipal OAuth2User principal, HttpSession session) {
//		try {
//			if (principal == null) {
//				String email = (String) session.getAttribute("USER_EMAIL");
//				if (email != null) {
//					GoogleUser user = googleUserRepository.findByEmail(email);
//					if (user != null) {
//						Map<String, Object> userInfo = new HashMap<>();
//						userInfo.put("id", user.getGoogleId());
//						userInfo.put("email", user.getEmail());
//						userInfo.put("name", user.getName());
//						userInfo.put("picture", user.getPicture());
//						return ResponseEntity.ok(userInfo);
//					}
//				}
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is logged in.");
//			}
//
//			Map<String, Object> userInfo = new HashMap<>();
//			userInfo.put("id", principal.getAttribute("sub"));
//			userInfo.put("email", principal.getAttribute("email"));
//			userInfo.put("name", principal.getAttribute("name"));
//			userInfo.put("picture", principal.getAttribute("picture"));
//
//			return ResponseEntity.ok(userInfo);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error checking auth status: " + e.getMessage());
//		}
//	}
//}
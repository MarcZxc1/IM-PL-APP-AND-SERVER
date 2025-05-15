//package com.qcu.fitnesstracker.controller;
//
//import com.qcu.fitnesstracker.model.User;
//import com.qcu.fitnesstracker.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users")
//@CrossOrigin(origins = "*")
//public class UserController {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	@PostMapping("/register")
//	public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userData) {
//		try {
//			String email = userData.get("email");
//			String password = userData.get("password");
//			String firstName = userData.get("firstName");
//			String lastName = userData.get("lastName");
//
//			if (email == null || password == null || firstName == null || lastName == null) {
//				return ResponseEntity.badRequest().body("All fields are required");
//			}
//
//			// Check if user already exists
//			if (userRepository.existsByEmail(email)) {
//				return ResponseEntity.badRequest().body("Email already registered");
//			}
//
//			// Create new user
//			User user = new User();
//			user.setEmail(email);
//			user.setPassword(passwordEncoder.encode(password));
//			user.setFirstName(firstName);
//			user.setLastName(lastName);
//
//			// Save the user
//			User savedUser = userRepository.save(user);
//
//			// Create response without password
//			Map<String, Object> response = new HashMap<>();
//			response.put("id", savedUser.getId());
//			response.put("firstName", savedUser.getFirstName());
//			response.put("lastName", savedUser.getLastName());
//			response.put("email", savedUser.getEmail());
//
//			return ResponseEntity.ok(response);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.internalServerError().body("An error occurred during registration: " + e.getMessage());
//		}
//	}
//
//	@PostMapping("/login")
//	public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
//		try {
//			String email = credentials.get("email");
//			String password = credentials.get("password");
//
//			if (email == null || password == null) {
//				return ResponseEntity.badRequest().body("Email and password are required");
//			}
//
//			// Authenticate user
//			Authentication authentication = authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(email, password)
//			);
//
//			// Get user info
//			User user = userRepository.findByEmail(email);
//			if (user == null) {
//				return ResponseEntity.badRequest().body("User not found");
//			}
//
//			// Create response without password
//			Map<String, Object> response = new HashMap<>();
//			response.put("id", user.getId());
//			response.put("firstName", user.getFirstName());
//			response.put("lastName", user.getLastName());
//			response.put("email", user.getEmail());
//
//			return ResponseEntity.ok(response);
//		} catch (AuthenticationException e) {
//			return ResponseEntity.status(401).body("Invalid credentials");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.internalServerError().body("An error occurred during login: " + e.getMessage());
//		}
//	}
//
//	@GetMapping("/user")
//	public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
//		if (principal == null) {
//			return ResponseEntity.status(401).body("No user is logged in.");
//		}
//
//		Map<String, Object> userInfo = new HashMap<>();
//		userInfo.put("email", principal.getAttribute("email"));
//		userInfo.put("name", principal.getAttribute("name"));
//		userInfo.put("picture", principal.getAttribute("picture"));
//		userInfo.put("id", principal.getAttribute("sub"));
//
//		return ResponseEntity.ok(userInfo);
//	}
//}
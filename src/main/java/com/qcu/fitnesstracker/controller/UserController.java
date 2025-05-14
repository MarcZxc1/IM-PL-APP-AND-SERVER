package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.model.User;
import com.qcu.fitnesstracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		// Check if user already exists
		if (userRepository.existsByEmail(user.getEmail())) {
			return ResponseEntity.badRequest().body("Email already registered");
		}

		// Save the user
		User savedUser = userRepository.save(user);

		// Create response without password
		Map<String, Object> response = new HashMap<>();
		response.put("id", savedUser.getId());
		response.put("firstName", savedUser.getFirstName());
		response.put("lastName", savedUser.getLastName());
		response.put("email", savedUser.getEmail());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
		String email = credentials.get("email");
		String password = credentials.get("password");

		User user = userRepository.findByEmail(email);

		if (user == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		if (!password.equals(user.getPassword())) {
			return ResponseEntity.badRequest().body("Invalid password");
		}

		// Create response without password
		Map<String, Object> response = new HashMap<>();
		response.put("id", user.getId());
		response.put("firstName", user.getFirstName());
		response.put("lastName", user.getLastName());
		response.put("email", user.getEmail());

		return ResponseEntity.ok(response);
	}
}
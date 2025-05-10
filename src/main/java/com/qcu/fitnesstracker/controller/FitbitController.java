package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.service.FitbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fitbit")
public class FitbitController {

	@Autowired
	private FitbitService fitbitService;

	private String latestAccessToken;

	@GetMapping("/auth")
	public void login(HttpServletResponse response) throws IOException {
		String authUrl = fitbitService.getAuthorizationUrl();
		response.sendRedirect(authUrl);
	}

	@GetMapping("/callback")
	public ResponseEntity<String> getAccessToken(@RequestParam String code) throws IOException {
		String accessToken = fitbitService.getAccessToken(code);
		// Save it for later retrieval
		this.latestAccessToken = accessToken; // <-- ADD THIS LINE
		System.out.println("Access Token: " + accessToken);
		return ResponseEntity.ok(accessToken);
	}

	@GetMapping("/token")
	public String getAccessToken() {
		return latestAccessToken;
	}

	@GetMapping("/steps")
	public ResponseEntity<String> getSteps(@RequestParam String accessToken, @RequestParam String date) {
		String result = fitbitService.getSteps(accessToken, date);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/energy")
	public ResponseEntity<String> getCalories(@RequestParam String accessToken, @RequestParam String date) {
		String result = fitbitService.getCalories(accessToken, date);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/caloriesIn")
	public ResponseEntity<String> getCaloriesIn(@RequestParam String accessToken, @RequestParam String date) {
		String result = fitbitService.getCaloriesIn(accessToken, date);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/sleep")
	public ResponseEntity<String> getSleep(@RequestParam String accessToken, @RequestParam String date) {
		String result = fitbitService.getSleep(accessToken, date);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/status")
	public Map<String, Object> getStatus() {
		Map<String, Object> status = new HashMap<>();
		status.put("loggedIn", latestAccessToken != null);
		return status;
	}
}
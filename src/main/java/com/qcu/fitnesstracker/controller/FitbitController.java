package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.service.FitbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/fitbit")
public class FitbitController {

	@Autowired
	private FitbitService fitbitService;

	@GetMapping("/auth")
	public void login(HttpServletResponse response) throws IOException {
		String authUrl = fitbitService.getAuthorizationUrl();
		response.sendRedirect(authUrl);
	}

	@GetMapping("/callback")
	public String getAccessToken(@RequestParam String code) {
		// Get access token from Fitbit service
		String accessToken = fitbitService.getAccessToken(code);

		// You can now use this access token to make further API calls (e.g., get steps or calories)
		return "Access Token: " + accessToken;
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
}
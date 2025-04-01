package com.qcu.fitnesstracker.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/api/strava")
public class StravaAuthController {

	@Value("${strava.clientId}")
	private String CLIENT_ID;

	@Value("${strava.clientSecret}")
	private String CLIENT_SECRET;

	@Value("${strava.redirectUri}")
	private String REDIRECT_URI;

	private static final String TOKEN_URL = "https://www.strava.com/oauth/token";
	private String accessToken;
	private String refreshToken;
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String TOKEN_FILE = "tokens.txt";

	public StravaAuthController() {
		loadTokens();
	}

	@GetMapping("/test")
	public String test() {
		return "It works!";
	}

	@GetMapping("/login")
	public RedirectView loginToStrava() {
		String url = "https://www.strava.com/oauth/authorize?client_id=" + CLIENT_ID +
				"&response_type=code&redirect_uri=" + REDIRECT_URI + "&scope=activity:read_all";
		return new RedirectView(url);
	}

	@GetMapping("/callback")
	public ResponseEntity<String> handleCallback(@RequestParam String code) {
		try {
			Map<String, Object> tokens = exchangeCodeForToken(code);
			if (tokens == null || !tokens.containsKey("access_token")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("⚠️ Invalid authorization code. Please log in again.");
			}
			accessToken = (String) tokens.get("access_token");
			refreshToken = (String) tokens.get("refresh_token");
			saveTokens();
			return ResponseEntity.ok("✅ Successfully authenticated!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("❌ Error during token exchange: " + e.getMessage());
		}
	}

	private Map<String, Object> exchangeCodeForToken(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", CLIENT_ID);
		params.add("client_secret", CLIENT_SECRET);
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", REDIRECT_URI);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<Map> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, Map.class);
		return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
	}

	@GetMapping("/refresh-token")
	public ResponseEntity<String> refreshAccessToken() {
		if (refreshToken == null || refreshToken.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ No refresh token available. Please reauthorize.");
		}
		try {
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("client_id", CLIENT_ID);
			params.add("client_secret", CLIENT_SECRET);
			params.add("grant_type", "refresh_token");
			params.add("refresh_token", refreshToken);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
			ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(response.getBody());

				accessToken = jsonNode.path("access_token").asText();
				refreshToken = jsonNode.path("refresh_token").asText();
				saveTokens();
				return ResponseEntity.ok("✅ Token refreshed successfully!");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Failed to refresh token.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error: " + e.getMessage());
		}
	}

	@GetMapping("/token")
	public ResponseEntity<Map<String, String>> getToken() {
		return ResponseEntity.ok(Map.of("access_token", getAccessToken()));
	}

	private String getAccessToken() {
		if (accessToken == null || accessToken.isEmpty()) {
			refreshAccessToken();
		}
		return accessToken;
	}

	private void saveTokens() {
		try (FileWriter writer = new FileWriter(TOKEN_FILE)) {
			writer.write(accessToken + "\n" + refreshToken);
		} catch (IOException e) {
			System.err.println("❌ Failed to save tokens: " + e.getMessage());
		}
	}

	private void loadTokens() {
		try (BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE))) {
			accessToken = reader.readLine();
			refreshToken = reader.readLine();
		} catch (IOException e) {
			System.err.println("⚠️ No existing tokens found.");
		}
	}
}

package com.qcu.fitnesstracker.service;

import com.qcu.fitnesstracker.model.FitbitTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class FitbitService {

	// Base URL for Fitbit API
	private final String API_URL = "https://api.fitbit.com/1/user/-/";

	// Injecting values from application.properties
	@Value("${fitbit.client.id}")
	private String clientId;

	@Value("${fitbit.client.secret}")
	private String clientSecret;

	@Value("${fitbit.redirect.uri}")
	private String redirectUri;

	// Generate the URL to redirect the user to the Fitbit OAuth2 authorization page
	public String getAuthorizationUrl() {
		return "https://www.fitbit.com/oauth2/authorize" +
				"?response_type=code" +
				"&client_id=" + clientId +
				"&redirect_uri=" + redirectUri +
				"&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight" +
				"&expires_in=604800";  // Token expiration time (7 days)
	}

	// Retrieve access token using the authorization code
	public String getAccessToken(String code) {
		RestTemplate restTemplate = new RestTemplate();

		// Set up HTTP headers, including client credentials for Basic Authentication
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(clientId, clientSecret);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Create the request body with the required parameters
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("redirect_uri", redirectUri);
		body.add("code", code);

		// Create the HTTP request entity with the body and headers
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

		// Make the POST request to exchange the authorization code for an access token
		ResponseEntity<FitbitTokenResponse> response = restTemplate.exchange(
				"https://api.fitbit.com/oauth2/token",  // Token endpoint
				HttpMethod.POST,
				entity,
				FitbitTokenResponse.class
		);

		// Return the access token from the response body
		return response.getBody().getAccess_token();
	}

	// Fetch step count data for a specific date
	public String getSteps(String accessToken, String date) {
		String url = API_URL + "activities/steps/date/" + date + "/1d.json";
		return fetchFromFitbit(url, accessToken);
	}

	// Fetch calories burned data for a specific date
	public String getCalories(String accessToken, String date) {
		String url = API_URL + "activities/calories/date/" + date + "/1d.json";
		return fetchFromFitbit(url, accessToken);
	}

	// Helper method to make the API call to Fitbit
	private String fetchFromFitbit(String url, String accessToken) {
		RestTemplate restTemplate = new RestTemplate();

		// Set up HTTP headers with the Bearer token for authentication
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		// Create the HTTP entity with headers
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// Make the GET request to the provided URL
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		// Return the response body (JSON data)
		return response.getBody();
	}
}

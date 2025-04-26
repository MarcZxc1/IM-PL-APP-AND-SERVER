package com.qcu.fitnesstracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/nutrition")
@CrossOrigin(origins = "*")
public class NutritionController {

	private static final Logger logger = LoggerFactory.getLogger(NutritionController.class);

	private final String API_KEY = "Z0gz2WrmxNXeEhtfgSrB8Uvsut5SRk6vOuc4JzPc"; // Replace with your USDA API key
	private final String BASE_URL = "https://api.nal.usda.gov/fdc/v1/foods/search";

	@GetMapping
	public ResponseEntity<String> searchFood(@RequestParam String query) {
		// Build the request URL
		String url = BASE_URL + "?query=" + query + "&api_key=" + API_KEY;

		// Initialize RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		try {
			// Make the API call and get the response
			String response = restTemplate.getForObject(url, String.class);

			// Log the successful response (you can refine this based on your needs)
			logger.info("API request successful for query: " + query);

			return ResponseEntity.ok(response);
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			// Log the error response
			logger.error("API request failed for query: " + query, ex);
			return ResponseEntity.status(ex.getStatusCode()).body("Error: " + ex.getMessage());
		} catch (Exception ex) {
			// Log any other unforeseen errors
			logger.error("Unexpected error occurred", ex);
			return ResponseEntity.status(500).body("Unexpected error occurred: " + ex.getMessage());
		}
	}
}

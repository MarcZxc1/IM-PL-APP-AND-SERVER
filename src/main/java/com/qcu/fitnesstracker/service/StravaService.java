package com.qcu.fitnesstracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class StravaService {

	private static final String STRAVA_API_URL = "https://www.strava.com/api/v3/athlete/activities";



	@Autowired
	private RestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(StravaService.class);

	/**
	 * Fetches the list of activities from Strava.
	 *
	 * @param accessToken the OAuth access token for Strava API
	 * @return JSON response as a String
	 */
	public String getActivities(String accessToken) {
		if (accessToken == null || accessToken.isEmpty()) {
			throw new IllegalArgumentException("Access token is required to fetch activities");
		}

		String url = STRAVA_API_URL + "?access_token=" + accessToken;

		try {
			return restTemplate.getForObject(url, String.class);
		} catch (RestClientException e) {
			logger.error("Error fetching activities from Strava API", e);
			return "Error fetching activities. Please check your access token or network connection.";
		}
	}
}
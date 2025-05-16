package com.qcu.fitnesstracker.service;

import com.qcu.fitnesstracker.model.GoogleUser;
import com.qcu.fitnesstracker.repository.GoogleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class GoogleOAuthService {

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirectUri;

	private final RestTemplate restTemplate;
	private final GoogleUserRepository googleUserRepository;

	@Autowired
	public GoogleOAuthService(RestTemplate restTemplate, GoogleUserRepository googleUserRepository) {
		this.restTemplate = restTemplate;
		this.googleUserRepository = googleUserRepository;
	}

	public String getAuthorizationUrl() {
		return UriComponentsBuilder
				.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
				.queryParam("client_id", clientId)
				.queryParam("redirect_uri", redirectUri)
				.queryParam("response_type", "code")
				.queryParam("scope", "email%20profile") // <-- FIXED HERE
				.queryParam("access_type", "offline")
				.queryParam("prompt", "consent")
				.build()
				.toUriString();
	}

	public Map<String, Object> exchangeCodeForToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", code);
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("redirect_uri", redirectUri);
		body.add("grant_type", "authorization_code");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		return restTemplate.postForObject(
				"https://oauth2.googleapis.com/token",
				request,
				Map.class
		);
	}

	public Map<String, Object> getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<?> request = new HttpEntity<>(headers);

		return restTemplate.getForObject(
				"https://www.googleapis.com/oauth2/v3/userinfo",
				Map.class
		);
	}

	public GoogleUser saveOrUpdateUser(Map<String, Object> userInfo) {
		String googleId = (String) userInfo.get("sub");
		String email = (String) userInfo.get("email");
		String name = (String) userInfo.get("name");
		String picture = (String) userInfo.get("picture");

		GoogleUser user = googleUserRepository.findByEmail(email);
		if (user == null) {
			user = new GoogleUser();
			user.setGoogleId(googleId);
			user.setEmail(email);
		}

		user.setName(name);
		user.setPicture(picture);

		return googleUserRepository.save(user);
	}
}

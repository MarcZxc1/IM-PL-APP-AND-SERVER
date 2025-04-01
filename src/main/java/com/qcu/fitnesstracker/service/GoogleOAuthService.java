//package com.qcu.fitnesstracker.service;
//
//import org.json.JSONObject;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class GoogleOAuthService {
//
//	public String exchangeCodeForToken(String code) {
//		String tokenEndpoint = "https://oauth2.googleapis.com/token";
//		String clientId = "826790037879-2nlt9hun7ciru6jvfa46io4s7d90f1lb.apps.googleusercontent.com";
//		String clientSecret = "client_secret_826790037879-2nlt9hun7ciru6jvfa46io4s7d90f1lb.apps.googleusercontent.com.json";
//		String redirectUri = "http://localhost:8080/login/oauth2/code/google";
//		String grantType = "authorization_code";
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//		body.add("code", code);
//		body.add("client_id", clientId);
//		body.add("client_secret", clientSecret);
//		body.add("redirect_uri", redirectUri);
//		body.add("grant_type", grantType);
//
//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, requestEntity, String.class);
//
//		JSONObject jsonResponse = new JSONObject(response.getBody());
//		return jsonResponse.getString("access_token");
//	}
//
//	public String fetchUserInfo(String accessToken) {
//		String userInfoEndpoint = "https://www.googleapis.com/oauth2/v23/userinfo?access_token=" + accessToken;
//		RestTemplate restTemplate = new RestTemplate();
//		String userInfoResponse = restTemplate.getForObject(userInfoEndpoint, String.class);
//
//		JSONObject json = new JSONObject(userInfoResponse);
//		String name = json.getString("name");
//		String email = json.getString("email");
//
//		return "Welcome, " + name + "! Your email is: " + email;
//	}
//}

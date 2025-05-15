package com.qcu.fitnesstracker.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// AuthTokenStore.java (Singleton/Bean)
@Component
public class AuthTokenStore {
	private final Map<String, String> tokenToUsername = new ConcurrentHashMap<>();

	public void storeToken(String token, String username) {
		tokenToUsername.put(token, username);
	}

	public String getUsername(String token) {
		return tokenToUsername.get(token);
	}

	public boolean isValid(String token) {
		return tokenToUsername.containsKey(token);
	}

	public void remove(String token) {
		tokenToUsername.remove(token);
	}
}

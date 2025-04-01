package com.qcu.fitnesstracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "google_users")
public class GoogleUser {
	@Id
	private String googleId;
	private String email;
	private String name;
	private String picture;
	private String password; // ✅ Store plain password (not hashed)

	public GoogleUser() {}

	public GoogleUser(String googleId, String email, String name, String picture, String password) {
		this.googleId = googleId;
		this.email = email;
		this.name = name;
		this.picture = picture;
		this.password = password; // ✅ Store password as plain text
	}

	// ✅ Getters
	public String getGoogleId() { return googleId; }
	public String getEmail() { return email; }
	public String getName() { return name; }
	public String getPicture() { return picture; }
	public String getPassword() { return password; } // ✅ Return plain text password

	// ✅ Set password without hashing
	public void setPassword(String password) {
		this.password = password;
	}
}

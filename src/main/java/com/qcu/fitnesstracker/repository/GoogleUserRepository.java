package com.qcu.fitnesstracker.repository;

import com.qcu.fitnesstracker.model.GoogleUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleUserRepository extends MongoRepository<GoogleUser, String> {
	GoogleUser findByGoogleId(String googleId); // ✅ Find user by Google ID

	GoogleUser findByEmail(String email);
}

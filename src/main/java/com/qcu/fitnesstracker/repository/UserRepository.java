package com.qcu.fitnesstracker.repository;


import com.qcu.fitnesstracker.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);
	boolean existsByEmail(String email);

}

package com.qcu.fitnesstracker.repository;


import com.qcu.fitnesstracker.model.WorkoutDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends MongoRepository<WorkoutDTO, String> {

	@Query("{ location: { $near: { $geometry: { type: 'Point', coordinates: [ ?0, ?1 ] }, $maxDistance: ?2 } } }")
	List<WorkoutDTO> findWorkoutsWithinCircle(double longitude, double latitude, double radius);
}
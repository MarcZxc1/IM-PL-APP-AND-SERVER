package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.model.WorkoutDTO;
import com.qcu.fitnesstracker.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/workouts")
@CrossOrigin(origins = "*") // Allow frontend requests (adjust for security)
public class WorkoutController {

	private static final Logger LOGGER = Logger.getLogger(WorkoutController.class.getName());

	@Autowired
	private WorkoutRepository workoutRepository;

	/**
	 * ✅ Fetch all workouts (For testing purposes)
	 */
	@GetMapping
	public ResponseEntity<List<WorkoutDTO>> getAllWorkouts() {
		List<WorkoutDTO> workouts = workoutRepository.findAll();
		return ResponseEntity.ok(workouts);
	}

	/**
	 * ✅ Fetch nearby workouts using geospatial queries
	 */
	@GetMapping("/nearby")
	public ResponseEntity<Map<String, Object>> getNearbyWorkouts(
			@RequestParam double longitude,
			@RequestParam double latitude,
			@RequestParam double radius) {

		Map<String, Object> response = new HashMap<>();

		try {
			if (radius <= 0) {
				response.put("message", "Radius must be greater than zero.");
				return ResponseEntity.badRequest().body(response);
			}

			List<WorkoutDTO> workouts = workoutRepository.findWorkoutsWithinCircle(longitude, latitude, radius);

			if (workouts.isEmpty()) {
				response.put("message", "No workouts found within the given radius.");
				response.put("data", new ArrayList<>());
				return ResponseEntity.ok(response);
			}

			response.put("message", "Workouts found successfully.");
			response.put("data", workouts);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LOGGER.severe("Error fetching nearby workouts: " + e.getMessage());
			response.put("message", "Error fetching workouts.");
			return ResponseEntity.internalServerError().body(response);
		}
	}

	/**
	 * ✅ Fetch a single workout by ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getWorkoutById(@PathVariable String id) {
		Optional<WorkoutDTO> workout = workoutRepository.findById(id);

		if (workout.isPresent()) {
			return ResponseEntity.ok(workout.get()); // ✅ Return the workout if found
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Workout not found."); // ✅ Corrected 404 response
		}
	}


	/**
	 * ✅ Add a new workout
	 */
	@PostMapping
	public ResponseEntity<?> addWorkout(@RequestBody WorkoutDTO workout) {
		try {
			WorkoutDTO savedWorkout = workoutRepository.save(workout);
			return ResponseEntity.ok(savedWorkout);
		} catch (Exception e) {
			LOGGER.severe("Error saving workout: " + e.getMessage());
			return ResponseEntity.internalServerError().body("Error saving workout.");
		}
	}

	/**
	 * ✅ Delete a workout by ID
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteWorkout(@PathVariable String id) {
		if (!workoutRepository.existsById(id)) {
			return ResponseEntity.status(404).body("Workout not found.");
		}
		workoutRepository.deleteById(id);
		return ResponseEntity.ok("Workout deleted successfully.");
	}

	/**
	 * ✅ Test endpoint (For debugging purposes)
	 */
	@GetMapping("/test")
	public ResponseEntity<String> testEndpoint() {
		return ResponseEntity.ok("Test endpoint is working!");
	}
}
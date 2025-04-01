package com.qcu.fitnesstracker.controller;


import com.qcu.fitnesstracker.model.WorkoutDTO;
import com.qcu.fitnesstracker.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WorkoutWebSocketController {

	@Autowired
	private WorkoutService workoutService;

	@MessageMapping("/workout.add")
	@SendTo("/topic/workouts")
	public WorkoutDTO addWorkout(WorkoutDTO workout) throws Exception {
		// Convert `Workout` to `WorkoutDTO` before returning
		WorkoutDTO savedWorkout = workoutService.addWorkout(workout);

		WorkoutDTO dto = new WorkoutDTO();
		dto.setId(savedWorkout.getId());
		dto.setUserId(savedWorkout.getUserId());
		dto.setWorkoutType(savedWorkout.getWorkoutType());
		dto.setDuration(savedWorkout.getDuration());
		dto.setTimestamp(savedWorkout.getTimestamp());

		// Convert location
		if (savedWorkout.getLocation() != null) {
			WorkoutDTO.Location location = new WorkoutDTO.Location();
			location.setType(savedWorkout.getLocation().getType());
			location.setCoordinates(savedWorkout.getLocation().getCoordinates());
			dto.setLocation(location);
		}

		return dto;
	}
}

package com.qcu.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "workouts")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkoutDTO {

	@Id
	@Field("_id") // Ensures MongoDB's ObjectId is mapped correctly
	private String id;

	@JsonProperty("userId")
	private String userId;

	@JsonProperty("workoutType")
	private String workoutType;

	@JsonProperty("duration")
	private double duration;

	@JsonProperty("timestamp")
	private long timestamp;

	@JsonProperty("location")
	private Location location;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@Data
	public static class Location {
		@JsonProperty("type")
		private String type;

		@JsonProperty("coordinates")
		private double[] coordinates; // Changed from List<Double> to double[]
	}
}

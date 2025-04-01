package com.qcu.fitnesstracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcu.fitnesstracker.model.WorkoutDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class WorkoutService {
	private static final String API_URL = "http://localhost:8080/workouts";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public List<WorkoutDTO> fetchWorkouts() throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;

		try {
			URL url = new URL(API_URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error Code: " + conn.getResponseCode());
			}

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// Print response for debugging
			String response = reader.lines().reduce("", (acc, line) -> acc + line);
			System.out.println("Raw JSON Response: " + response);

			return Arrays.asList(objectMapper.readValue(response, WorkoutDTO[].class));
		} finally {
			if (reader != null) reader.close();
			if (conn != null) conn.disconnect();
		}
	}

	public WorkoutDTO addWorkout(WorkoutDTO workout) throws Exception {
		HttpURLConnection conn = null;
		OutputStream os = null;
		BufferedReader reader = null;

		try {
			URL url = new URL(API_URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			String jsonInputString = objectMapper.writeValueAsString(workout);
			os = conn.getOutputStream();
			os.write(jsonInputString.getBytes());
			os.flush();

			if (conn.getResponseCode() != 201) {
				throw new RuntimeException("HTTP POST Request Failed with Error Code: " + conn.getResponseCode());
			}

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			return objectMapper.readValue(reader, WorkoutDTO.class);
		} finally {
			if (os != null) {
				os.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
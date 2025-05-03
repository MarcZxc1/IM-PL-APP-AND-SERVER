package com.qcu.fitnesstracker.controller;

import com.qcu.fitnesstracker.model.Meals;
import com.qcu.fitnesstracker.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meals")
@CrossOrigin(origins = "*")
public class MealSuggestionController {

	@Autowired
	private MealRepository mealRepository;

	@GetMapping
	public ResponseEntity<List<Meals>> getAllMeals() {
		return ResponseEntity.ok(mealRepository.findAll());
	}

	@GetMapping("/suggest")
	public ResponseEntity<List<Map<String, Object>>> suggestMeal(
			@RequestParam String ingredient,
			@RequestParam(required = false) String mealType) {

		// Step 1: Search by ingredient
		List<Meals> matchedMeals = mealRepository.findByKeywordsContainingIgnoreCase(ingredient);

		// Step 2: Filter by mealType (bulking, cutting, etc.) if provided
		if (mealType != null && !mealType.isEmpty()) {
			matchedMeals = matchedMeals.stream()
					.filter(meal -> meal.getKeywords().stream()
							.anyMatch(k -> k.equalsIgnoreCase(mealType)))
					.collect(Collectors.collectingAndThen(
							Collectors.toMap(Meals::getMeal, m -> m, (m1, m2) -> m1),
							map -> map.values().stream().collect(Collectors.toList())
					));
			matchedMeals.forEach(m -> System.out.println("Matched: " + m.getMeal()));

		}

			// Step 3: Convert to List<Map<String, Object>> for JSON
		List<Map<String, Object>> responseList = matchedMeals.stream()
				.limit(4)
				.map(meal -> {
					Map<String, Object> map = new HashMap<>();
					map.put("meal", meal.getMeal());
					map.put("ingredients", meal.getIngredients());
					map.put("nutrition", meal.getNutrition());
					map.put("image", meal.getImage());
					return map;
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok(responseList);
	}
}
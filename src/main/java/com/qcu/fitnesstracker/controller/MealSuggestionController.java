package com.qcu.fitnesstracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/meals")
@CrossOrigin(origins = "*")
public class MealSuggestionController {


	private static final List<Map<String, Object>> MEALS = List.of(
			Map.of(
					"keywords", List.of("chicken", "bulking", "high protein"),
					"meal", "Chicken Power Bowl",
					"ingredients", List.of("Chicken Breast", "Brown Rice", "Steamed Broccoli", "Avocado", "Olive Oil"),
					"nutrition", Map.of("calories", 580, "protein", 48, "carbohydrates", 45, "fat", 22)
			),
			Map.of(
					"keywords", List.of("salmon", "maintenance"),
					"meal", "Salmon Quinoa Salad",
					"ingredients", List.of("Grilled Salmon", "Quinoa", "Cherry Tomatoes", "Spinach", "Feta Cheese"),
					"nutrition", Map.of("calories", 620, "protein", 40, "carbohydrates", 38, "fat", 30)
			),
			Map.of(
					"keywords", List.of("turkey", "cutting"),
					"meal", "Turkey Lettuce Wraps",
					"ingredients", List.of("Ground Turkey", "Romaine Lettuce", "Carrots", "Cucumber", "Low-fat Yogurt Sauce"),
					"nutrition", Map.of("calories", 350, "protein", 35, "carbohydrates", 18, "fat", 15)
			),
			Map.of(
					"keywords", List.of("beef", "bulking"),
					"meal", "Beef and Sweet Potato Hash",
					"ingredients", List.of("Lean Ground Beef", "Sweet Potato", "Spinach", "Bell Peppers", "Eggs"),
					"nutrition", Map.of("calories", 700, "protein", 50, "carbohydrates", 50, "fat", 35)
			),
			Map.of(
					"keywords", List.of("tofu", "vegetarian", "high protein"),
					"meal", "Tofu Stir-Fry",
					"ingredients", List.of("Tofu", "Broccoli", "Carrots", "Bell Peppers", "Brown Rice", "Soy Sauce"),
					"nutrition", Map.of("calories", 520, "protein", 30, "carbohydrates", 40, "fat", 20)
			),
			Map.of(
					"keywords", List.of("eggs", "cutting", "high protein"),
					"meal", "Egg White Omelet",
					"ingredients", List.of("Egg Whites", "Spinach", "Tomatoes", "Mushrooms", "Low-fat Cheese"),
					"nutrition", Map.of("calories", 300, "protein", 28, "carbohydrates", 10, "fat", 12)
			),
			Map.of(
					"keywords", List.of("tuna", "cutting", "lean"),
					"meal", "Tuna Avocado Salad",
					"ingredients", List.of("Canned Tuna", "Avocado", "Cucumber", "Mixed Greens", "Olive Oil"),
					"nutrition", Map.of("calories", 400, "protein", 35, "carbohydrates", 8, "fat", 25)
			),
			Map.of(
					"keywords", List.of("cottage cheese", "maintenance", "snack"),
					"meal", "Cottage Cheese & Fruit",
					"ingredients", List.of("Low-fat Cottage Cheese", "Blueberries", "Almonds", "Honey"),
					"nutrition", Map.of("calories", 320, "protein", 25, "carbohydrates", 20, "fat", 14)
			),
			Map.of(
					"keywords", List.of("protein shake", "bulking", "shake"),
					"meal", "Protein Mass Gainer Shake",
					"ingredients", List.of("Whey Protein", "Peanut Butter", "Banana", "Oats", "Milk"),
					"nutrition", Map.of("calories", 800, "protein", 55, "carbohydrates", 60, "fat", 30)
			),
			Map.of(
					"keywords", List.of("lentils", "vegan", "cutting"),
					"meal", "Lentil Veggie Bowl",
					"ingredients", List.of("Lentils", "Kale", "Tomatoes", "Carrots", "Avocado", "Olive Oil"),
					"nutrition", Map.of("calories", 450, "protein", 25, "carbohydrates", 35, "fat", 18)
			)
	);

	@GetMapping("/suggest")
	public ResponseEntity<Map<String, Object>> suggestMeal(@RequestParam String ingredient) {
		Map<String, Object> response = new HashMap<>();

		String lowerIngredient = ingredient.toLowerCase();
		Optional<Map<String, Object>> mealMatch = MEALS.stream()
				.filter(meal -> ((List<String>) meal.get("keywords")).stream().anyMatch(lowerIngredient::contains))
				.findFirst();

		if (mealMatch.isPresent()) {
			Map<String, Object> meal = mealMatch.get();
			response.put("meal", meal.get("meal"));
			response.put("ingredients", meal.get("ingredients"));
			response.put("nutrition", meal.get("nutrition"));
		} else {
			response.put("meal", "No meal found for ingredient");
			response.put("ingredients", Collections.emptyList());
			response.put("nutrition", Collections.emptyMap());
		}

		return ResponseEntity.ok(response);
	}
}

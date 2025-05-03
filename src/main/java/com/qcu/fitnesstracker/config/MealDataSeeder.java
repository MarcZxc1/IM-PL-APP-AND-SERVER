package com.qcu.fitnesstracker.config;

import com.qcu.fitnesstracker.model.Meals;
import com.qcu.fitnesstracker.repository.MealRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MealDataSeeder implements CommandLineRunner {

	private final MealRepository mealRepository;

	public MealDataSeeder(MealRepository mealRepository) {
		this.mealRepository = mealRepository;
	}

	@Override
	public void run(String... args) {
		List<Meals> meals = List.of(
				new Meals("Chicken Power Bowl", List.of("chicken", "bulking", "high protein"),
						List.of("Chicken Breast", "Brown Rice", "Steamed Broccoli", "Avocado", "Olive Oil"),
						Map.of("calories", 580, "protein", 48, "carbohydrates", 45, "fat", 22),
						"chicken_power_bowl.jpg"),

				new Meals("Shrimp and Zucchini Noodles", List.of("shrimp", "cutting", "low calorie"),
						List.of("Shrimp", "Zucchini Noodles", "Garlic", "Olive Oil", "Lemon"),
						Map.of("calories", 280, "protein", 30, "carbohydrates", 12, "fat", 10),
						"shrimp_zoodles.jpg"),

				// Filipino Meals
				new Meals("Chicken Adobo", List.of("filipino", "chicken", "cutting", "weight loss"),
						List.of("Chicken Thighs", "Soy Sauce", "Vinegar", "Garlic", "Bay Leaves", "Pepper"),
						Map.of("calories", 320, "protein", 28, "carbohydrates", 5, "fat", 20),
						"chicken_adobo.jpg"),

				new Meals("Paksiw na Bangus", List.of("filipino", "fish", "weight loss", "low carb"),
						List.of("Milkfish", "Vinegar", "Garlic", "Ginger", "Eggplant"),
						Map.of("calories", 220, "protein", 24, "carbohydrates", 4, "fat", 12),
						"paksiw_na_bangus.jpg"),

				new Meals("Sinigang na Baboy", List.of("filipino", "pork", "maintenance"),
						List.of("Pork", "Tamarind", "Kangkong", "Radish", "Tomato", "Green Beans"),
						Map.of("calories", 400, "protein", 30, "carbohydrates", 10, "fat", 25),
						"sinigang_na_baboy.jpg"),

				new Meals("Tortang Talong", List.of("filipino", "vegetarian", "cutting"),
						List.of("Eggplant", "Eggs", "Onion", "Garlic", "Salt"),
						Map.of("calories", 250, "protein", 12, "carbohydrates", 8, "fat", 18),
						"tortang_talong.jpg"),

				new Meals("Tuna Kinilaw", List.of("filipino", "fish", "cutting", "weight loss", "low carb"),
						List.of("Tuna", "Vinegar", "Calamansi", "Onion", "Ginger", "Chili"),
						Map.of("calories", 180, "protein", 25, "carbohydrates", 3, "fat", 7),
						"kinilaw_na_tuna.jpg"),

				// International Meals
				new Meals("Beef and Quinoa Bowl", List.of("beef", "bulking", "high protein"),
						List.of("Ground Beef", "Quinoa", "Bell Peppers", "Spinach", "Olive Oil"),
						Map.of("calories", 610, "protein", 40, "carbohydrates", 42, "fat", 28),
						"beef_quinoa_bowl.jpg"),

				new Meals("Grilled Salmon with Veggies", List.of("salmon", "maintenance", "omega-3"),
						List.of("Salmon", "Broccoli", "Carrots", "Olive Oil", "Lemon"),
						Map.of("calories", 500, "protein", 38, "carbohydrates", 18, "fat", 30),
						"grilled_salmon.jpg"),

				new Meals("Avocado Egg Toast", List.of("avocado", "egg", "cutting", "breakfast"),
						List.of("Whole Grain Bread", "Avocado", "Boiled Egg", "Lemon", "Salt"),
						Map.of("calories", 320, "protein", 14, "carbohydrates", 22, "fat", 20),
						"avocado_egg_toast.jpg"),

				new Meals("Vegetable Stir Fry", List.of("vegetarian", "weight loss", "cutting"),
						List.of("Broccoli", "Carrot", "Bell Pepper", "Tofu", "Soy Sauce"),
						Map.of("calories", 290, "protein", 18, "carbohydrates", 20, "fat", 12),
						"vegetable_stir_fry.jpg"),
				// ➕ Bulking Meals
				new Meals("Steak and Sweet Potato", List.of("beef", "bulking", "high protein"),
						List.of("Sirloin Steak", "Sweet Potato", "Spinach", "Olive Oil"),
						Map.of("calories", 700, "protein", 55, "carbohydrates", 40, "fat", 35),
						"steak_sweet_potato.jpg"),

				new Meals("Peanut Butter Banana Smoothie", List.of("smoothie", "bulking", "high calorie"),
						List.of("Banana", "Peanut Butter", "Milk", "Protein Powder", "Honey"),
						Map.of("calories", 650, "protein", 35, "carbohydrates", 50, "fat", 28),
						"pb_banana_smoothie.jpg"),

				new Meals("Egg and Cheese Burrito", List.of("egg", "bulking", "breakfast"),
						List.of("Whole Wheat Tortilla", "Eggs", "Cheese", "Avocado", "Salsa"),
						Map.of("calories", 580, "protein", 30, "carbohydrates", 35, "fat", 32),
						"egg_cheese_burrito.jpg"),

				// ➕ Cutting Meals
				new Meals("Turkey Lettuce Wraps", List.of("turkey", "cutting", "low carb"),
						List.of("Ground Turkey", "Lettuce", "Carrot", "Cucumber", "Soy Sauce"),
						Map.of("calories", 260, "protein", 30, "carbohydrates", 10, "fat", 12),
						"turkey_lettuce_wraps.jpg"),

				new Meals("Zucchini Egg Muffins", List.of("egg", "cutting", "low calorie", "breakfast"),
						List.of("Eggs", "Zucchini", "Onion", "Cheese", "Salt"),
						Map.of("calories", 180, "protein", 15, "carbohydrates", 5, "fat", 10),
						"zucchini_egg_muffins.jpg"),

				new Meals("Grilled Chicken Salad", List.of("chicken", "cutting", "low fat"),
						List.of("Chicken Breast", "Lettuce", "Tomato", "Cucumber", "Vinaigrette"),
						Map.of("calories", 300, "protein", 35, "carbohydrates", 10, "fat", 12),
						"grilled_chicken_salad.jpg"),

				// ➕ Maintenance Meals
				new Meals("Tuna Sandwich", List.of("tuna", "maintenance", "lunch"),
						List.of("Whole Wheat Bread", "Tuna", "Lettuce", "Tomato", "Low-fat Mayo"),
						Map.of("calories", 450, "protein", 30, "carbohydrates", 35, "fat", 18),
						"tuna_sandwich.jpg"),

				new Meals("Chicken Fried Rice", List.of("chicken", "maintenance", "balanced"),
						List.of("Chicken", "Brown Rice", "Egg", "Peas", "Carrot", "Soy Sauce"),
						Map.of("calories", 520, "protein", 35, "carbohydrates", 45, "fat", 20),
						"chicken_fried_rice.jpg"),

				new Meals("Spaghetti with Turkey Meatballs", List.of("turkey", "maintenance", "pasta"),
						List.of("Whole Wheat Pasta", "Turkey", "Tomato Sauce", "Garlic", "Parmesan"),
						Map.of("calories", 500, "protein", 35, "carbohydrates", 50, "fat", 15),
						"turkey_spaghetti.jpg"),

				// ➕ Weight Loss Meals
				new Meals("Cauliflower Rice Bowl", List.of("vegetarian", "weight loss", "low carb"),
						List.of("Cauliflower Rice", "Tofu", "Broccoli", "Bell Pepper", "Soy Sauce"),
						Map.of("calories", 240, "protein", 18, "carbohydrates", 12, "fat", 10),
						"cauliflower_rice_bowl.jpg"),

				new Meals("Boiled Egg and Cucumber Snack", List.of("egg", "weight loss", "snack"),
						List.of("Boiled Eggs", "Cucumber", "Salt", "Pepper"),
						Map.of("calories", 180, "protein", 14, "carbohydrates", 3, "fat", 11),
						"egg_cucumber_snack.jpg"),

				new Meals("Baked Cod with Asparagus", List.of("fish", "weight loss", "low fat"),
						List.of("Cod", "Asparagus", "Lemon", "Olive Oil", "Herbs"),
						Map.of("calories", 270, "protein", 32, "carbohydrates", 5, "fat", 10),
						"baked_cod_asparagus.jpg"),
				// 🍗 Chicken Meals
				new Meals("Lemon Herb Chicken",
						List.of("chicken", "cutting", "low fat"),
						List.of("Chicken Breast", "Lemon Juice", "Garlic", "Rosemary", "Olive Oil"),
						Map.of("calories", 290, "protein", 40, "carbohydrates", 2, "fat", 12),
						"lemon_herb_chicken.jpg"
				),
				new Meals("Chicken Caesar Wrap",
						List.of("chicken", "maintenance", "lunch"),
						List.of("Tortilla", "Grilled Chicken", "Romaine Lettuce", "Caesar Dressing", "Parmesan"),
						Map.of("calories", 430, "protein", 32, "carbohydrates", 25, "fat", 20),
						"chicken_caesar_wrap.jpg"
				),
				new Meals("Spicy Grilled Chicken", List.of("chicken", "cutting", "low fat"), List.of("Chicken Breast", "Chili Powder", "Lime Juice", "Garlic", "Olive Oil"), Map.of("calories", 280, "protein", 39, "carbohydrates", 3, "fat", 10), "spicy_grilled_chicken.jpg"),
				new Meals("Teriyaki Chicken Bowl", List.of("chicken", "bulking", "asian"), List.of("Chicken Thigh", "Teriyaki Sauce", "Broccoli", "Rice", "Carrots"), Map.of("calories", 520, "protein", 35, "carbohydrates", 50, "fat", 18), "teriyaki_chicken_bowl.jpg"),
				new Meals("Chicken Tikka Masala", List.of("chicken", "maintenance", "indian"), List.of("Chicken", "Tomatoes", "Cream", "Spices", "Rice"), Map.of("calories", 600, "protein", 40, "carbohydrates", 20, "fat", 30), "chicken_tikka_masala.jpg"),
				new Meals("BBQ Chicken Salad", List.of("chicken", "cutting", "salad"), List.of("Grilled Chicken", "Lettuce", "Corn", "Black Beans", "BBQ Sauce"), Map.of("calories", 350, "protein", 32, "carbohydrates", 15, "fat", 14), "bbq_chicken_salad.jpg"),
				new Meals("Honey Mustard Chicken", List.of("chicken", "maintenance", "baked"), List.of("Chicken Breast", "Honey", "Mustard", "Garlic", "Herbs"), Map.of("calories", 430, "protein", 36, "carbohydrates", 18, "fat", 20), "honey_mustard_chicken.jpg"),
				new Meals("Buffalo Chicken Wrap", List.of("chicken", "bulking", "lunch"), List.of("Grilled Chicken", "Tortilla", "Hot Sauce", "Lettuce", "Ranch"), Map.of("calories", 520, "protein", 38, "carbohydrates", 25, "fat", 24), "buffalo_chicken_wrap.jpg"),
				new Meals("Chicken Fajita Bowl", List.of("chicken", "cutting", "mexican"), List.of("Chicken Strips", "Peppers", "Onions", "Rice", "Avocado"), Map.of("calories", 480, "protein", 35, "carbohydrates", 40, "fat", 20), "chicken_fajita_bowl.jpg"),
				new Meals("Crispy Chicken Sandwich", List.of("chicken", "bulking", "fast food"), List.of("Chicken Breast", "Bun", "Pickles", "Mayonnaise", "Lettuce"), Map.of("calories", 620, "protein", 32, "carbohydrates", 45, "fat", 32), "crispy_chicken_sandwich.jpg"),
				new Meals("Mediterranean Chicken Plate", List.of("chicken", "maintenance", "greek"), List.of("Grilled Chicken", "Quinoa", "Tomatoes", "Cucumber", "Feta"), Map.of("calories", 490, "protein", 42, "carbohydrates", 30, "fat", 18), "mediterranean_chicken_plate.jpg"),
				new Meals("Orange Chicken", List.of("chicken", "bulking", "asian"), List.of("Chicken", "Orange Sauce", "Rice", "Green Onion", "Sesame Seeds"), Map.of("calories", 560, "protein", 34, "carbohydrates", 55, "fat", 20), "orange_chicken.jpg"),


				// 🥩 Beef Meals
				new Meals("Beef Stroganoff",
						List.of("beef", "maintenance", "pasta"),
						List.of("Beef Sirloin", "Mushrooms", "Onion", "Sour Cream", "Egg Noodles"),
						Map.of("calories", 620, "protein", 40, "carbohydrates", 45, "fat", 30),
						"beef_stroganoff.jpg"
				),
				new Meals("Beef Veggie Stir Fry",
						List.of("beef", "cutting", "low carb"),
						List.of("Beef Strips", "Bell Pepper", "Broccoli", "Garlic", "Soy Sauce"),
						Map.of("calories", 390, "protein", 38, "carbohydrates", 10, "fat", 20),
						"beef_veggie_stirfry.jpg"
				),
				new Meals("Classic Beef Burger", List.of("beef", "bulking", "fast food"), List.of("Beef Patty", "Bun", "Cheddar", "Lettuce", "Tomato"), Map.of("calories", 700, "protein", 40, "carbohydrates", 40, "fat", 35), "classic_beef_burger.jpg"),
				new Meals("Beef Burrito Bowl", List.of("beef", "maintenance", "mexican"), List.of("Ground Beef", "Rice", "Beans", "Salsa", "Avocado"), Map.of("calories", 610, "protein", 38, "carbohydrates", 45, "fat", 28), "beef_burrito_bowl.jpg"),
				new Meals("Korean Beef Bowl", List.of("beef", "cutting", "asian"), List.of("Ground Beef", "Soy Sauce", "Garlic", "Green Onion", "Broccoli"), Map.of("calories", 410, "protein", 35, "carbohydrates", 15, "fat", 20), "korean_beef_bowl.jpg"),
				new Meals("Beef Ramen", List.of("beef", "bulking", "japanese"), List.of("Beef", "Ramen Noodles", "Egg", "Broth", "Green Onion"), Map.of("calories", 650, "protein", 40, "carbohydrates", 50, "fat", 28), "beef_ramen.jpg"),
				new Meals("Steak and Potatoes", List.of("beef", "maintenance", "dinner"), List.of("Steak", "Potatoes", "Garlic", "Butter", "Green Beans"), Map.of("calories", 580, "protein", 45, "carbohydrates", 30, "fat", 25), "steak_and_potatoes.jpg"),
				new Meals("Beef Chili", List.of("beef", "cutting", "comfort food"), List.of("Ground Beef", "Beans", "Tomato", "Onions", "Chili Powder"), Map.of("calories", 460, "protein", 38, "carbohydrates", 18, "fat", 22), "beef_chili.jpg"),
				new Meals("Beef and Quinoa Bowl", List.of("beef", "cutting", "high protein"), List.of("Beef Strips", "Quinoa", "Zucchini", "Spinach", "Garlic"), Map.of("calories", 470, "protein", 40, "carbohydrates", 25, "fat", 20), "beef_quinoa_bowl.jpg"),
				new Meals("Stuffed Bell Peppers", List.of("beef", "maintenance", "vegetable"), List.of("Ground Beef", "Bell Peppers", "Rice", "Tomato Sauce", "Cheese"), Map.of("calories", 530, "protein", 36, "carbohydrates", 28, "fat", 24), "stuffed_bell_peppers.jpg"),
				new Meals("Mongolian Beef", List.of("beef", "bulking", "chinese"), List.of("Beef", "Soy Sauce", "Brown Sugar", "Garlic", "Green Onion"), Map.of("calories", 580, "protein", 35, "carbohydrates", 35, "fat", 28), "mongolian_beef.jpg"),
				new Meals("Beef Meatballs with Zoodles", List.of("beef", "cutting", "low carb"), List.of("Ground Beef", "Zucchini", "Marinara Sauce", "Parmesan", "Herbs"), Map.of("calories", 420, "protein", 38, "carbohydrates", 10, "fat", 22), "beef_zoodles.jpg"),


				// 🥓 Pork Meals
				new Meals("Garlic Pork Belly",
						List.of("pork", "bulking", "filipino"),
						List.of("Pork Belly", "Garlic", "Soy Sauce", "Vinegar", "Sugar"),
						Map.of("calories", 700, "protein", 35, "carbohydrates", 5, "fat", 60),
						"garlic_pork_belly.jpg"
				),
				new Meals("Pork and Cabbage Stir Fry",
						List.of("pork", "cutting", "low calorie"),
						List.of("Lean Ground Pork", "Cabbage", "Ginger", "Garlic", "Carrots"),
						Map.of("calories", 340, "protein", 30, "carbohydrates", 12, "fat", 18),
						"pork_cabbage_stirfry.jpg"
				),

				// 🍖 Lamb Meals
				new Meals("Grilled Lamb Chops",
						List.of("lamb", "bulking", "protein-rich"),
						List.of("Lamb Chops", "Garlic", "Rosemary", "Olive Oil", "Salt"),
						Map.of("calories", 580, "protein", 45, "carbohydrates", 0, "fat", 40),
						"grilled_lamb_chops.jpg"
				),
				new Meals("Lamb Stew",
						List.of("lamb", "maintenance", "comfort"),
						List.of("Lamb", "Potatoes", "Carrots", "Onions", "Tomato Paste"),
						Map.of("calories", 540, "protein", 38, "carbohydrates", 30, "fat", 28),
						"lamb_stew.jpg"
				),

				new Meals("Lamb Gyro Wrap", List.of("lamb", "maintenance", "greek"), List.of("Lamb", "Pita", "Tzatziki", "Onion", "Tomato"), Map.of("calories", 540, "protein", 38, "carbohydrates", 30, "fat", 28), "lamb_gyro_wrap.jpg"),
				new Meals("Lamb and Couscous", List.of("lamb", "bulking", "mediterranean"), List.of("Lamb", "Couscous", "Zucchini", "Chickpeas", "Mint"), Map.of("calories", 600, "protein", 42, "carbohydrates", 40, "fat", 26), "lamb_and_couscous.jpg"),
				new Meals("Lamb Curry", List.of("lamb", "maintenance", "indian"), List.of("Lamb", "Yogurt", "Tomatoes", "Spices", "Onion"), Map.of("calories", 520, "protein", 38, "carbohydrates", 15, "fat", 28), "lamb_curry.jpg"),
				new Meals("Moroccan Lamb Tagine", List.of("lamb", "maintenance", "north african"), List.of("Lamb", "Apricots", "Cinnamon", "Onions", "Chickpeas"), Map.of("calories", 580, "protein", 36, "carbohydrates", 35, "fat", 26), "lamb_tagine.jpg"),
				new Meals("Lamb Burger", List.of("lamb", "bulking", "fast food"), List.of("Ground Lamb", "Bun", "Feta", "Tomato", "Lettuce"), Map.of("calories", 670, "protein", 40, "carbohydrates", 35, "fat", 34), "lamb_burger.jpg"),
				new Meals("Herb Crusted Rack of Lamb", List.of("lamb", "bulking", "fine dining"), List.of("Rack of Lamb", "Rosemary", "Breadcrumbs", "Garlic", "Mustard"), Map.of("calories", 720, "protein", 45, "carbohydrates", 10, "fat", 45), "rack_of_lamb.jpg"),
				new Meals("Lamb Meatballs", List.of("lamb", "cutting", "italian"), List.of("Ground Lamb", "Parsley", "Parmesan", "Tomato Sauce", "Garlic"), Map.of("calories", 480, "protein", 36, "carbohydrates", 10, "fat", 30), "lamb_meatballs.jpg"),
				new Meals("Lamb Pita Pockets", List.of("lamb", "maintenance", "mediterranean"), List.of("Lamb", "Pita", "Cucumber", "Yogurt", "Lettuce"), Map.of("calories", 520, "protein", 34, "carbohydrates", 28, "fat", 26), "lamb_pita_pockets.jpg"),
				new Meals("Spicy Lamb Stir Fry", List.of("lamb", "cutting", "asian"), List.of("Lamb", "Bell Pepper", "Chili", "Ginger", "Soy Sauce"), Map.of("calories", 440, "protein", 38, "carbohydrates", 8, "fat", 22), "spicy_lamb_stirfry.jpg"),
				new Meals("Lamb Souvlaki", List.of("lamb", "cutting", "greek"), List.of("Lamb", "Lemon", "Oregano", "Onion", "Skewers"), Map.of("calories", 390, "protein", 36, "carbohydrates", 4, "fat", 20), "lamb_souvlaki.jpg"),


				// 🥦 Vegetable-Based Meals
				new Meals("Roasted Veggie Bowl",
						List.of("vegetarian", "weight loss", "high fiber"),
						List.of("Zucchini", "Bell Pepper", "Sweet Potato", "Chickpeas", "Tahini"),
						Map.of("calories", 350, "protein", 15, "carbohydrates", 40, "fat", 14),
						"roasted_veggie_bowl.jpg"
				),
				new Meals("Spinach Mushroom Omelette",
						List.of("vegetarian", "cutting", "breakfast"),
						List.of("Eggs", "Spinach", "Mushrooms", "Onion", "Feta Cheese"),
						Map.of("calories", 300, "protein", 20, "carbohydrates", 6, "fat", 20),
						"spinach_mushroom_omelette.jpg"
				),
				new Meals("Quinoa Chickpea Salad", List.of("vegetarian", "maintenance", "high fiber"), List.of("Quinoa", "Chickpeas", "Cucumber", "Lemon", "Mint"), Map.of("calories", 350, "protein", 18, "carbohydrates", 35, "fat", 12), "quinoa_chickpea_salad.jpg"),
				new Meals("Vegetable Stir Fry", List.of("vegetarian", "cutting", "low calorie"), List.of("Broccoli", "Carrot", "Bell Pepper", "Tofu", "Soy Sauce"), Map.of("calories", 320, "protein", 18, "carbohydrates", 22, "fat", 15), "vegetable_stir_fry.jpg"),
				new Meals("Sweet Potato Black Bean Bowl", List.of("vegetarian", "bulking", "fiber-rich"), List.of("Sweet Potato", "Black Beans", "Corn", "Avocado", "Lime"), Map.of("calories", 460, "protein", 20, "carbohydrates", 55, "fat", 16), "sweet_potato_black_bean_bowl.jpg"),
				new Meals("Zucchini Noodle Bowl", List.of("vegetarian", "weight loss", "low carb"), List.of("Zucchini", "Tomatoes", "Pesto", "Parmesan", "Pine Nuts"), Map.of("calories", 290, "protein", 15, "carbohydrates", 12, "fat", 18), "zucchini_noodle_bowl.jpg"),
				new Meals("Eggplant Parmesan", List.of("vegetarian", "maintenance", "baked"), List.of("Eggplant", "Tomato Sauce", "Mozzarella", "Breadcrumbs", "Basil"), Map.of("calories", 430, "protein", 22, "carbohydrates", 25, "fat", 22), "eggplant_parmesan.jpg"),
				new Meals("Lentil Veggie Soup", List.of("vegetarian", "cutting", "comfort food"), List.of("Lentils", "Carrots", "Celery", "Tomato", "Spices"), Map.of("calories", 300, "protein", 20, "carbohydrates", 30, "fat", 8), "lentil_veggie_soup.jpg"),
				new Meals("Stuffed Zucchini Boats", List.of("vegetarian", "cutting", "low carb"), List.of("Zucchini", "Quinoa", "Tomatoes", "Mozzarella", "Herbs"), Map.of("calories", 350, "protein", 18, "carbohydrates", 20, "fat", 14), "stuffed_zucchini_boats.jpg"),
				new Meals("Vegetable Fried Rice", List.of("vegetarian", "bulking", "asian"), List.of("Rice", "Carrots", "Peas", "Egg", "Soy Sauce"), Map.of("calories", 480, "protein", 16, "carbohydrates", 60, "fat", 18), "vegetable_fried_rice.jpg"),
				new Meals("Tofu Power Bowl", List.of("vegetarian", "maintenance", "high protein"), List.of("Tofu", "Quinoa", "Spinach", "Carrots", "Peanut Sauce"), Map.of("calories", 470, "protein", 28, "carbohydrates", 30, "fat", 22), "tofu_power_bowl.jpg"),
				new Meals("Broccoli Cheddar Bake", List.of("vegetarian", "comfort", "high fat"), List.of("Broccoli", "Cheddar", "Eggs", "Cream", "Breadcrumbs"), Map.of("calories", 420, "protein", 22, "carbohydrates", 15, "fat", 28), "broccoli_cheddar_bake.jpg")


				);

		for (Meals meal : meals) {
			try {
				mealRepository.save(meal);
				System.out.println("✅ Seeded: " + meal.getMeal());
			} catch (DuplicateKeyException e) {
				System.out.println("⚠️ Skipped (duplicate): " + meal.getMeal());
			}
		}
	}
}

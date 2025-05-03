package com.qcu.fitnesstracker.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "meals")
public class Meals {

	@Id
	private String id;

	private String meal;
	private List<String> keywords;
	private List<String> ingredients;
	private Map<String, Object> nutrition;
	private String image;

	public Meals(String meal, List<String> keywords, List<String> ingredients,
				Map<String, Object> nutrition, String image) {
		this.meal = meal;
		this.keywords = keywords;
		this.ingredients = ingredients;
		this.nutrition = nutrition;
		this.image = image;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeal() {
		return meal;
	}

	public void setMeal(String meal) {
		this.meal = meal;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keyword) {
		this.keywords = keyword;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public Map<String, Object> getNutrition() {
		return nutrition;
	}

	public void setNutrition(Map<String, Object> nutrition) {
		this.nutrition = nutrition;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

//	public List<String> getKeywords() {
//		return keywords;
//	}
//
//	public void setKeywords(List<String> keywords) {
//		this.keywords = keywords;
//	}


}

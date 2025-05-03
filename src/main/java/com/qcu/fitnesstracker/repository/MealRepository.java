package com.qcu.fitnesstracker.repository;

import com.qcu.fitnesstracker.model.Meals;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends MongoRepository<Meals, String> {
//	List<Meals> findByKeywordsContainingIgnoreCase(String ingredient);
   List<Meals> findByKeywordsContainingIgnoreCase(String keyword);
	List<Meals> findByKeywordsContainingIgnoreCaseAndKeywordsContainingIgnoreCase(String ingredient, String mealType);

}

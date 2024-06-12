package com.recipro.service;

import com.recipro.model.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipesByUser(String userId);

    void addRecipeToUser(String userId, Recipe recipe);

    //If a recipe no longer has an associated user after removal, recipe is deleted from the database
    void removeRecipeFromUser(String userId, String recipeId);
}
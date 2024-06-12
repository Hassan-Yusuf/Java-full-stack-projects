package com.recipro.dao;

import com.recipro.model.Recipe;
import com.recipro.model.User;

import java.util.List;

public interface RecipeDao {

    List<Recipe> getRecipesByUser(String userId);

    Recipe createNewRecipe(Recipe recipe);

    Recipe findRecipeById(String id);

    void deleteRecipe(String id);

    void deleteRecipeFromUser(String recipeId, String userId);

    void addRecipeToUser(String recipeId, String userId);

    List<Recipe> getAllRecipes();

    int getRecipeUserCount(String id);
}
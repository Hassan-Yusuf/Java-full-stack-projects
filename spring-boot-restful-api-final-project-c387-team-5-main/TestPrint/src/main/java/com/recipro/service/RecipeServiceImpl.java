package com.recipro.service;

import com.recipro.dao.UserDao;
import com.recipro.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import com.recipro.dao.RecipeDao;
import com.recipro.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService{

    @Autowired
    private UserDao userDao;
    private RecipeDao recipeDao;

    @Autowired
    public RecipeServiceImpl(RecipeDao recipeDao, UserDao userDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
    }

    @Override
    public List<Recipe> getRecipesByUser(String userId) {
        // Retrieve and return the user's recipes
        return recipeDao.getRecipesByUser(userId);
    }

    @Override
    // Add a recipe to a user's list of recipes
    public void addRecipeToUser(String userId, Recipe recipe) {
        if(recipeDao.findRecipeById(recipe.getRecipeId()) == null){
            recipeDao.createNewRecipe(recipe);
        }
        // Add the recipe to the user's list of recipes
        recipeDao.addRecipeToUser(recipe.getRecipeId(), userId);
    }

    @Override
    public void removeRecipeFromUser(String userId, String recipeId) {
        // Remove the recipe from the user's list of recipes
        recipeDao.deleteRecipeFromUser(recipeId, userId);

        // If recipe is no longer associated with a user, delete it
        if(recipeDao.getRecipeUserCount(recipeId) == 0){
            recipeDao.deleteRecipe(recipeId);
        }
    }
}
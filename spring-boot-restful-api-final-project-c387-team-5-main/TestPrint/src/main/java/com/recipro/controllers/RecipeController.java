package com.recipro.controllers;

import com.recipro.model.Recipe;
import com.recipro.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @GetMapping("/{userId}")
    public List<Recipe> getRecipe(@PathVariable String userId) {
        return recipeService.getRecipesByUser(userId);
    }

    @PostMapping("/{userId}")
    public Recipe addRecipeToUser(@PathVariable String userId, @RequestBody Recipe recipe) {
        recipeService.addRecipeToUser(userId, recipe);
        return recipe;
    }
    @DeleteMapping("/{userId}")
    public Recipe deleteRecipe(@PathVariable String userId, @RequestBody Recipe recipe) {
        recipeService.removeRecipeFromUser(userId, recipe.getRecipeId());
        return recipe;
    }
}
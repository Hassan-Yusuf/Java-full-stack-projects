package com.recipro.dao;

import com.recipro.TestApplicationConfiguration;
import com.recipro.model.Recipe;
import com.recipro.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class RecipeDaoImplTest {
    @Autowired
    RecipeDao recipeDao;

    @Autowired
    UserDao userDao;

    @BeforeEach
    public void setUp(){
        List<Recipe> recipes = recipeDao.getAllRecipes();

        for(Recipe r: recipes) recipeDao.deleteRecipe(r.getRecipeId());

        List<String> users = userDao.getAllUsers();

        for(String u: users) userDao.deleteUser(u);
    }

    @Test
    public void testAddGetRecipe() {
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("1");

        recipeDao.createNewRecipe(recipe1);

        Recipe returnedRecipe = recipeDao.findRecipeById(recipe1.getRecipeId());

        assertEquals(recipe1,returnedRecipe, "Returned recipe should be the same");
    }

    @Test
    public void testAddGetAllRecipes(){
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("1");
        Recipe recipe2 = new Recipe();
        recipe1.setRecipeId("2");

        recipeDao.createNewRecipe(recipe1);
        recipeDao.createNewRecipe(recipe2);

        List<Recipe> recipes = recipeDao.getAllRecipes();

        assertEquals(2,recipes.size(), "List should be 2 long");

        assertTrue(recipes.contains(recipe1),"List should contain first recipe");
        assertTrue(recipes.contains(recipe2), "List should contain second recipe");
    }

    @Test
    public void testDeleteRecipe(){
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("1");
        Recipe recipe2 = new Recipe();
        recipe1.setRecipeId("2");

        recipeDao.createNewRecipe(recipe1);
        recipeDao.createNewRecipe(recipe2);

        recipeDao.deleteRecipe(recipe1.getRecipeId());

        List<Recipe> recipes = recipeDao.getAllRecipes();

        assertEquals(1,recipes.size(), "List should be of size 1 after removal");

        assertFalse(recipes.contains(recipe1),"List should NOT contain first recipe");
        assertTrue(recipes.contains(recipe2), "List should contain second recipe");

        recipeDao.deleteRecipe(recipe2.getRecipeId());

        recipes = recipeDao.getAllRecipes();

        assertTrue(recipes.isEmpty(), "List should be empty after all recipes removed");
    }

    @Test
    public void testAddGetUserRecipe(){
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("1");
        Recipe recipe2 = new Recipe();
        recipe1.setRecipeId("2");

        User user1 = new User();
        user1.setUserId("user1");
        user1.setPassword("pass1");

        userDao.createNewUser(user1);

        recipeDao.createNewRecipe(recipe1);

        recipeDao.addRecipeToUser(recipe1.getRecipeId(), user1.getUserId());

        List<Recipe> userRecipes = recipeDao.getRecipesByUser(user1.getUserId());

        assertEquals(1,userRecipes.size(),"User should have 1 recipe");
        assertTrue(userRecipes.contains(recipe1), "User should have recipe1");

        recipeDao.createNewRecipe(recipe2);
        recipeDao.addRecipeToUser(recipe2.getRecipeId(),user1.getUserId());

        userRecipes = recipeDao.getRecipesByUser(user1.getUserId());

        assertEquals(2, userRecipes.size(), "User should have 2 recipes");
        assertTrue(userRecipes.contains(recipe2),"User should have recipe2");
    }

    @Test
    public void testRemoveUserRecipe(){
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("5");
        Recipe recipe2 = new Recipe();
        recipe1.setRecipeId("6");

        User user1 = new User();
        user1.setUserId("user1");
        user1.setPassword("pass1");

        userDao.createNewUser(user1);

        recipeDao.createNewRecipe(recipe1);
        recipeDao.createNewRecipe(recipe2);

        recipeDao.addRecipeToUser(recipe2.getRecipeId(),user1.getUserId());
        recipeDao.addRecipeToUser(recipe1.getRecipeId(), user1.getUserId());

        recipeDao.deleteRecipeFromUser(recipe1.getRecipeId(),user1.getUserId());

        List<Recipe> userRecipes = recipeDao.getRecipesByUser(user1.getUserId());

        assertEquals(1,userRecipes.size(),"User should have 1 recipe");
        assertFalse(userRecipes.contains(recipe1), "User should NOT have recipe1");
        assertTrue(userRecipes.contains(recipe2),"User should have recipe2");

        recipeDao.deleteRecipeFromUser(recipe2.getRecipeId(),user1.getUserId());
        userRecipes = recipeDao.getRecipesByUser(user1.getUserId());

        assertTrue(userRecipes.isEmpty(),"User should have no recipes");

        // Check recipes are stored
        List<Recipe> recipes = recipeDao.getAllRecipes();
        assertEquals(2,recipes.size(), "List should be 2 long");

        assertTrue(recipes.contains(recipe1),"List should contain first recipe");
        assertTrue(recipes.contains(recipe2), "List should contain second recipe");

    }

    @Test
    public void testRecipeUsers(){
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("7");
        Recipe recipe2 = new Recipe();
        recipe1.setRecipeId("8");

        User user1 = new User();
        user1.setUserId("user1");
        user1.setPassword("pass1");
        User user2 = new User();
        user2.setUserId("user2");
        user2.setPassword("pass2");

        userDao.createNewUser(user1);
        userDao.createNewUser(user2);

        recipeDao.createNewRecipe(recipe1);
        recipeDao.createNewRecipe(recipe2);

        recipeDao.addRecipeToUser(recipe1.getRecipeId(),user1.getUserId());
        recipeDao.addRecipeToUser(recipe1.getRecipeId(),user2.getUserId());
        recipeDao.addRecipeToUser(recipe2.getRecipeId(),user1.getUserId());

        int recipeUsers = recipeDao.getRecipeUserCount(recipe1.getRecipeId());

        assertEquals(2,recipeUsers,"Recipe 1 should have two users");

        recipeUsers = recipeDao.getRecipeUserCount(recipe2.getRecipeId());

        assertEquals(1,recipeUsers,"Recipe 2 should have one user");
    }

}
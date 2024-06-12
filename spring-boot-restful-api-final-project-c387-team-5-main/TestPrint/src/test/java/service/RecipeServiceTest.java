package service;

import com.recipro.TestApplicationConfiguration;
import com.recipro.dao.RecipeDao;
import com.recipro.dao.UserDao;
import com.recipro.model.Recipe;
import com.recipro.model.User;
import com.recipro.service.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
@ActiveProfiles("test")
public class RecipeServiceTest {

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RecipeServiceImpl recipeService;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        List<Recipe> recipes = recipeDao.getAllRecipes();
        for (Recipe recipe : recipes) {
            recipeDao.deleteRecipe(recipe.getRecipeId());
        }

        List<String> users = userDao.getAllUsers();
        for (String userId : users) {
            userDao.deleteUser(userId);
        }
    }

    @Test
    public void testGetRecipesByUser() {
        User user = new User();
        user.setUserId("user1");
        user.setPassword("password");

        userDao.createNewUser(user);

        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("1");

        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId("2");

        recipeDao.createNewRecipe(recipe1);
        recipeDao.createNewRecipe(recipe2);

        recipeService.addRecipeToUser("user1", recipe1);
        recipeService.addRecipeToUser("user1", recipe2);

        List<Recipe> userRecipes = recipeService.getRecipesByUser("user1");

        // Debug output
        System.out.println("Expected recipe IDs: 1, 2");
        System.out.println("Recipes from service:");
        for (Recipe r : userRecipes) {
            System.out.println(r);
        }

        System.out.println("Recipe1: " + recipe1);
        System.out.println("Recipe2: " + recipe2);

        assertEquals(2, userRecipes.size());
        assertTrue(userRecipes.contains(recipe1));
        assertTrue(userRecipes.contains(recipe2));
    }

    @Test
    public void testAddRecipeToUser() {
        User user = new User();
        user.setUserId("user2");
        user.setPassword("password");

        userDao.createNewUser(user);

        Recipe recipe = new Recipe();
        recipe.setRecipeId("3");

        recipeDao.createNewRecipe(recipe);

        recipeService.addRecipeToUser("user2", recipe);

        List<Recipe> userRecipes = recipeService.getRecipesByUser("user2");

        // Debug output
        System.out.println("Expected recipe ID: 3");
        System.out.println("Recipes from service:");
        for (Recipe r : userRecipes) {
            System.out.println(r);
        }

        System.out.println("Recipe: " + recipe);

        assertEquals(1, userRecipes.size());
        assertTrue(userRecipes.contains(recipe));
    }

    @Test
    public void testRemoveRecipeFromUser() {
        User user = new User();
        user.setUserId("user3");
        user.setPassword("password");

        userDao.createNewUser(user);

        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("4");

        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId("5");

        recipeDao.createNewRecipe(recipe1);
        recipeDao.createNewRecipe(recipe2);

        recipeService.addRecipeToUser("user3", recipe1);
        recipeService.addRecipeToUser("user3", recipe2);

        recipeService.removeRecipeFromUser("user3", "4");

        List<Recipe> userRecipes = recipeService.getRecipesByUser("user3");

        // Debug output
        System.out.println("Expected remaining recipe ID: 5");
        System.out.println("Recipes from service:");
        for (Recipe r : userRecipes) {
            System.out.println(r);
        }

        assertEquals(1, userRecipes.size());
        assertFalse(userRecipes.contains(recipe1));
        assertTrue(userRecipes.contains(recipe2));

        // Check if the recipe is deleted from the database if no users are associated with it
        int recipeUserCount = recipeDao.getRecipeUserCount("4");
        if (recipeUserCount == 0) {
            Recipe deletedRecipe = recipeDao.findRecipeById("4");
            assertNull(deletedRecipe);
        }
    }

    @Test
    public void testGetAllRecipes() {
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId("1");
        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId("2");

        recipeDao.createNewRecipe(recipe1);
        recipeDao.createNewRecipe(recipe2);

        List<Recipe> recipes = recipeDao.getAllRecipes();

        // Debug output
        System.out.println("Expected recipe IDs: 1, 2");
        System.out.println("Recipes from service:");
        for (Recipe r : recipes) {
            System.out.println(r);
        }

        assertEquals(2, recipes.size());
        assertTrue(recipes.contains(recipe1));
        assertTrue(recipes.contains(recipe2));
    }

    @Test
    public void testGetRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setRecipeId("1");

        recipeDao.createNewRecipe(recipe);

        Recipe foundRecipe = recipeDao.findRecipeById("1");

        // Debug output
        System.out.println("Expected recipe ID: 1");
        if (foundRecipe != null) {
            System.out.println("Found recipe ID: " + foundRecipe.getRecipeId());
        }

        assertEquals(recipe, foundRecipe);
    }

    @Test
    public void testDeleteRecipe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeId("1");

        recipeDao.createNewRecipe(recipe);

        recipeDao.deleteRecipe("1");

        Recipe foundRecipe = recipeDao.findRecipeById("1");

        // Debug output
        System.out.println("Expected to find no recipe");
        if (foundRecipe != null) {
            System.out.println("Found recipe ID: " + foundRecipe.getRecipeId());
        }

        assertNull(foundRecipe);
    }

    @Test
    public void testGetRecipeUserCount() {
        // Create users
        User user1 = new User();
        user1.setUserId("user1");
        user1.setPassword("password");
        userDao.createNewUser(user1);

        User user2 = new User();
        user2.setUserId("user2");
        user2.setPassword("password");
        userDao.createNewUser(user2);

        // Create and add recipe
        Recipe recipe = new Recipe();
        recipe.setRecipeId("1");

        recipeDao.createNewRecipe(recipe);

        recipeService.addRecipeToUser("user1", recipe);
        recipeService.addRecipeToUser("user2", recipe);

        int userCount = recipeDao.getRecipeUserCount(recipe.getRecipeId());

        // Debug output
        System.out.println("Expected user count: 2");
        System.out.println("Actual user count: " + userCount);

        assertEquals(2, userCount);
    }

    /*@Test
    public void testRemoveRecipeFromUserAndDeleteIfNoUsers() {
        // Create a user and add them to the database
        User user1 = new User();
        user1.setUserId("user1");
        user1.setPassword("password");
        userDao.createNewUser(user1);

        // Create another user and add them to the database
        User user2 = new User();
        user2.setUserId("user2");
        user2.setPassword("password");
        userDao.createNewUser(user2);

        // Create a recipe and add it to the database
        Recipe recipe = new Recipe();
        recipe.setRecipeId("recipe1");
        recipeDao.createNewRecipe(recipe);

        // Add the recipe to both users
        recipeService.addRecipeToUser("user1", recipe);
        recipeService.addRecipeToUser("user2", recipe);

        // Verify the recipe is associated with both users
        assertEquals(1, recipeService.getRecipesByUser("user1").size());
        assertEquals(1, recipeService.getRecipesByUser("user2").size());

        // Remove the recipe from user1
        recipeService.removeRecipeFromUser("user1", "recipe1");

        // Verify the recipe is still in the database because user2 still has it
        Recipe recipeFromDb = recipeDao.findRecipeById("recipe1");
        assertNotNull(recipeFromDb);

        // Remove the recipe from user2
        recipeService.removeRecipeFromUser("user2", "recipe1");

        // Verify the recipe is no longer in the database because no users have it
        recipeFromDb = recipeDao.findRecipeById("recipe1");
        assertNull(recipeFromDb);
    }

*/


}

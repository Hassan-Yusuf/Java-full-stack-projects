package com.recipro.dao;

import com.recipro.TestApplicationConfiguration;
import com.recipro.model.Recipe;
import com.recipro.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class UserDaoImplTest {
    @Autowired
    UserDao userDao;
    @Autowired
    RecipeDao recipeDao;
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() { //Resets the database before each test
        List<String> users = userDao.getAllUsers();
        for(String user : users){
            userDao.deleteUser(user);
        }
        List<Recipe> recipes = recipeDao.getAllRecipes();
        for(Recipe recipe : recipes){
            recipeDao.deleteRecipe(recipe.getRecipeId());
        }
    }

    @Test
    public void testAddGetUser(){ //This tests for when user is added and then retrieved using getUserById
        User user = new User();
        user.setUserId("noobo");
        user.setPassword("password");
        userDao.createNewUser(user);
        User fromDB = userDao.getUserById("noobo");
        assertEquals("noobo", fromDB.getUserId());}

    @Test
    public void testDeleteUser(){ //This tests for when user is deleted
        User user = new User();
        user.setUserId("noobo");
        user.setPassword("password");
        userDao.createNewUser(user);
        Recipe recipe = new Recipe();
        recipe.setRecipeId("1");
        recipeDao.createNewRecipe(recipe);
        recipeDao.addRecipeToUser("1", "noobo");
        userDao.deleteUser("noobo");
        List<Recipe> userRecipeList = recipeDao.getRecipesByUser("noobo");
        User checkFromDB = userDao.getUserById("noobo");
        assertNull(checkFromDB);
        assertEquals(0, userRecipeList.size());
    }

    @Test
    public void testGetAllUsers(){ //This tests for when there are multiple users and checks if they are all returned
        User user = new User();
        user.setUserId("noobo");
        user.setPassword("password");
        userDao.createNewUser(user);
        User user2 = new User();
        user2.setUserId("noobo2");
        user2.setPassword("password2");
        userDao.createNewUser(user2);
        List<String> users = userDao.getAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains("noobo"));
        assertTrue(users.contains("noobo2"));
    }

    @Test
    public void testVerifyCorrectUser(){ // This tests for when user and pass combo is correct
        User user = new User();
        user.setUserId("noobo");
        user.setPassword("password"); //
        userDao.createNewUser(user);
        String userId = userDao.verifyUser(user);
        assertEquals("noobo", userId);
    }

    @Test
    public void testVerifyIncorrectUser(){ // This test for when user and pass combo is incorrect
        User user = new User();
        user.setUserId("noobo");
        user.setPassword("password");
        String userId = userDao.verifyUser(user);
        assertNull(userId);
    }

}

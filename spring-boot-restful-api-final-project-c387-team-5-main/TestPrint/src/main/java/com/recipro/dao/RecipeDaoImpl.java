package com.recipro.dao;

import com.recipro.dao.mappers.RecipeMapper;
import com.recipro.dao.mappers.UserMapper;
import com.recipro.model.Recipe;
import com.recipro.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;

@Repository
public class RecipeDaoImpl implements RecipeDao{

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public RecipeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> getRecipesByUser(String userId) {
        final String GET_RECIPES = "SELECT ur.recipeId FROM User u" +
                " INNER JOIN UserRecipe ur ON u.userName = ur.userName" +
                " WHERE u.userName = ?";

        return jdbcTemplate.query(GET_RECIPES,new RecipeMapper(), userId);
    }

    @Override
    public Recipe createNewRecipe(Recipe recipe) {
        final String INSERT_RECIPE = "INSERT INTO Recipe (recipeId)" +
                " VALUES (?)";
        jdbcTemplate.update(INSERT_RECIPE, recipe.getRecipeId());
        return recipe;
    }

    @Override
    public Recipe findRecipeById(String id) {
        try {
            final String SELECT_RECIPE_BY_ID = "SELECT * FROM Recipe" +
                    " WHERE recipeId = ?";
            return jdbcTemplate.queryForObject(SELECT_RECIPE_BY_ID, new RecipeMapper(), id);
        }catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public void deleteRecipe(String id) {
        // Make sure no users have the recipe
        final String DELETE_RECIPE_USER = "DELETE FROM UserRecipe WHERE recipeId = ?";
        jdbcTemplate.update(DELETE_RECIPE_USER, id);

        final String DELETE_RECIPE = "DELETE FROM Recipe WHERE recipeId = ?";
        jdbcTemplate.update(DELETE_RECIPE, id);
    }

    @Override
    public void deleteRecipeFromUser(String recipeId, String userId) {
        final String DELETE_USER_RECIPE = "DELETE FROM UserRecipe WHERE recipeId = ? && userName = ?";
        jdbcTemplate.update(DELETE_USER_RECIPE, recipeId, userId);
    }

    @Override
    public void addRecipeToUser(String recipeId, String userId) {
        final String ADD_RECIPE_USER = "INSERT INTO UserRecipe (userName, recipeId)" +
                " VALUES (?,?)";
        jdbcTemplate.update(ADD_RECIPE_USER, userId, recipeId);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        final String GET_ALL_RECIPES = "SELECT * FROM Recipe";
        return jdbcTemplate.query(GET_ALL_RECIPES, new RecipeMapper());
    }

    @Override
    public int getRecipeUserCount(String id) {
        try {
            final String GET_RECIPE_USERS = "SELECT count(*) FROM Recipe r " +
                    "INNER JOIN UserRecipe ur ON r.recipeId = ur.recipeId " +
                    "WHERE r.recipeId = ? " +
                    "GROUP BY r.recipeId";
            return jdbcTemplate.queryForObject(GET_RECIPE_USERS, Integer.class, id);
        }catch (DataAccessException e){
            return 0;
        }
    }
}
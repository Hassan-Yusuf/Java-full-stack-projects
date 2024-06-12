package com.recipro.dao;

import com.recipro.dao.mappers.UserMapper;
import com.recipro.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @Autowired
    private final JdbcTemplate jdbc;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public User getUserById(String id) {
        try {
            final String GET_USER_ID = "SELECT * FROM User WHERE userName = ?";
            return jdbc.queryForObject(GET_USER_ID, new UserMapper(), id);
        }catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public User createNewUser(User user) {
        String password = passwordEncoder().encode(user.getPassword());
        final String INSERT_USER = "INSERT INTO User (username, password) VALUES (?, ?)";
        jdbc.update(INSERT_USER, user.getUserId(), password);
        return user;
    }



    @Override
    public String verifyUser(User user) {
        //@TODO Compare encrypted passwords
        try {
            final String SELECT_USER = "SELECT * FROM User WHERE userName = ?";
            User actualUser = jdbc.queryForObject(SELECT_USER, new UserMapper(), user.getUserId());
            System.out.println(actualUser.getPassword());
            System.out.println(user.getPassword());
            if (passwordEncoder().matches(user.getPassword(), actualUser.getPassword())) {
                System.out.println("User is valid");
                return user.getUserId();
            } else {
                System.out.println("User is not valid");
                return null;
            }
        }catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public void deleteUser(String id) {
        final String DELETE_RECIPE_USER = "DELETE FROM UserRecipe WHERE userName = ?";
        jdbc.update(DELETE_RECIPE_USER, id);
        final String DELETE_USER = "DELETE FROM User WHERE userName = ?";
        jdbc.update(DELETE_USER, id);
    }

    @Override
    public List<String> getAllUsers() {
        final String GET_ALL_IDS = "SELECT username FROM User";

        return jdbc.query(GET_ALL_IDS, (rs, rowNum) -> rs.getString("userName"));
    }

    private static PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
}

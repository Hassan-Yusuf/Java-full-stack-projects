package com.recipro.dao;

import com.recipro.model.User;

import java.util.List;

public interface UserDao {

    User getUserById(String id);

    User createNewUser(User user);

    String verifyUser(User user);

    void deleteUser(String id);

    List<String> getAllUsers();

}

package com.recipro.service;

import com.recipro.dao.UserVerificationException;
import com.recipro.model.User;

public interface UserService {
    // Check if user password pair exists
    String verifyUser(User user) throws UserVerificationException;

    String addUser(User user) throws UserVerificationException;

    void deleteUser(User user);
}

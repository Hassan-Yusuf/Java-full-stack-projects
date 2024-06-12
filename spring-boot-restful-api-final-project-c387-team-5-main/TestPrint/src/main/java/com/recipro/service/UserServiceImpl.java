package com.recipro.service;

import com.recipro.dao.UserVerificationException;
import com.recipro.model.User;
import com.recipro.dao.UserDao;
import com.recipro.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    // Field injection for UserDao
    @Autowired
    private UserDao userDao;


    // Constructor for any other dependencies
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public String verifyUser(User user) throws UserVerificationException{
        // Verify the user
        String verifiedUser = userDao.verifyUser(user);
        System.out.println(verifiedUser);
        // Check if the verification was successful
        if (verifiedUser == null) {
            throw new UserVerificationException("User id or password is incorrect");
        }else {
            return verifiedUser;
        }
    }

    @Override
    public String addUser(User user) throws UserVerificationException{
        if (user.getUserId() == null || user.getPassword() == null) {
            throw new UserVerificationException("User ID or password cannot be null");
        }
        if(userDao.getUserById(user.getUserId()) != null){
            throw new UserVerificationException("User with ID " + user.getUserId() + " already exists.");
        }
        return userDao.createNewUser(user).getUserId();
    }

    @Override
    public void deleteUser(User user) {
        // Delete the user
        userDao.deleteUser(user.getUserId());
    }
}


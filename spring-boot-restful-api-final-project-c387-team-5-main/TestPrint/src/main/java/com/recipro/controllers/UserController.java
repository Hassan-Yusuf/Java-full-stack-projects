package com.recipro.controllers;

import com.recipro.dao.UserVerificationException;
import com.recipro.model.User;
import com.recipro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping("/{userId}")
    public String verifyUser(@PathVariable String userId, @RequestParam String password) {
        User attemptedUser = new User();
        attemptedUser.setUserId(userId);
        attemptedUser.setPassword(password);
        try {
            return userService.verifyUser(attemptedUser);
        } catch (UserVerificationException e) {
            return null;
        }
    }
    @PostMapping("/{userId}")
    public String addUser(@PathVariable String userId,@RequestParam String password) {
        User attemptedUser = new User();
        attemptedUser.setUserId(userId);
        attemptedUser.setPassword(password);
        try {
            return userService.addUser(attemptedUser);
        } catch (UserVerificationException e) {
            return null;
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        User user = new User();
        user.setUserId(userId);
        userService.deleteUser(user);
    }

}
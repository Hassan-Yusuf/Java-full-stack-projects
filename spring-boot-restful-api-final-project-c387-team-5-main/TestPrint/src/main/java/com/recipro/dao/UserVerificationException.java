package com.recipro.dao;

public class UserVerificationException extends Exception{
    public UserVerificationException(String message) {
        super(message);
    }

    public UserVerificationException(String message,
                                           Throwable cause) {
        super(message, cause);
    }
}

package com.aseemsavio.blog.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User Not Found");
    }
}

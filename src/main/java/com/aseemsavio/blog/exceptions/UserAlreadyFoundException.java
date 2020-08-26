package com.aseemsavio.blog.exceptions;

public class UserAlreadyFoundException extends Exception {

    public UserAlreadyFoundException() {
        super("User already found in the Blog. Cannot signup with the same ID");
    }

}

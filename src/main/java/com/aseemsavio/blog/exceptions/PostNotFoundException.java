package com.aseemsavio.blog.exceptions;

public class PostNotFoundException extends Exception {

    public PostNotFoundException() {
        super("Could not locate the post to the provided Post ID");
    }
}

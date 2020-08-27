package com.aseemsavio.blog.exceptions;

public class CommentNotFoundException extends Exception {

    public CommentNotFoundException() {
        super("Could Not Find Comment for the provided ID");
    }
}

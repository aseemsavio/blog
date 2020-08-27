package com.aseemsavio.blog.exceptions;

public class DatabaseException extends Exception {

    public DatabaseException() {
        super("Error Occurred while writing to Blog DB");
    }
}

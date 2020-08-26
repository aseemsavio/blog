package com.aseemsavio.blog.exceptions;

public class SanityCheckFailedException extends Exception {

    public SanityCheckFailedException() {
        super("Sanity Checks Failed");
    }

}

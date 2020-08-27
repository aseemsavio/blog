package com.aseemsavio.blog.utils;

public interface BlogConstants {

    public final String EMPTY_STRING = "";
    public final String ACCESS_TOKEN_HEADER = "access-token";
    public final String SUCCESS = "SUCCESS";
    public final String FAILURE = "FAILURE";

    public final String SUCCESS_MESSAGE = "Operation Completed Successfully";
    public final String FAILURE_MESSAGE = "Operation Failed!";

    public final String EC_SANITY_CHECK = "01";
    public final String EM_SANITY_CHECK = "Sanity Check Failed. Please check your input data";

    public final String EC_USER_FOUND = "02";
    public final String EM_USER_FOUND = "User Already Found. Please try again with a different user name.";

    public final String EC_USER_NOT_FOUND = "02";
    public final String EM_USER_NOT_FOUND = "User Not Found. Please sign up before you login";

    public final String EC_DB_EXCEPTION = "03";
    public final String EM_DB_EXCEPTION = "Exception Occurred while writing to Blog Database";

    public final String EC_POST_NOT_FOUND = "04";
    public final String EM_POST_NOT_FOUND = "Could not find Post for the provided Post ID.";

    public final String EC_COMMENT_NOT_FOUND = "05";
    public final String EM_COMMENT_NOT_FOUND = "Could not find Comment for the provided Comment ID.";

    public final String EC_ACCESS_TOKEN_HEADER_NOT_FOUND = "06";
    public final String EM_ACCESS_TOKEN_HEADER_NOT_FOUND = "Could not find Access-Token Header in the request.";

}

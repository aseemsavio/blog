package com.aseemsavio.blog.aspects;

import com.aseemsavio.blog.exceptions.*;
import com.aseemsavio.blog.pojos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.aseemsavio.blog.utils.BlogConstants.*;

@ControllerAdvice("com.aseemsavio.blog.controllers")
public class RespondOnException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SanityCheckFailedException.class)
    public ResponseEntity<ErrorResponse> respondOnSanityChecksFailure() {
        ErrorResponse errorResponse = new ErrorResponse(EC_SANITY_CHECK, EM_SANITY_CHECK);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyFoundException.class)
    public ResponseEntity<ErrorResponse> respondOnUserAlreadyFound() {
        ErrorResponse errorResponse = new ErrorResponse(EC_USER_FOUND, EM_USER_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> respondOnUserNotFound() {
        ErrorResponse errorResponse = new ErrorResponse(EC_USER_NOT_FOUND, EM_USER_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> respondOnDatabaseException() {
        ErrorResponse errorResponse = new ErrorResponse(EC_DB_EXCEPTION, EM_DB_EXCEPTION);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> respondOnPostNotFoundException() {
        ErrorResponse errorResponse = new ErrorResponse(EC_POST_NOT_FOUND, EM_POST_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> respondOnCommentNotFoundException() {
        ErrorResponse errorResponse = new ErrorResponse(EC_COMMENT_NOT_FOUND, EM_COMMENT_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}

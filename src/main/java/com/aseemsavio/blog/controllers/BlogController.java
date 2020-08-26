package com.aseemsavio.blog.controllers;

import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.exceptions.UserAlreadyFoundException;
import com.aseemsavio.blog.exceptions.UserNotFoundException;
import com.aseemsavio.blog.pojos.SignInRequest;
import com.aseemsavio.blog.pojos.SignUpRequest;
import com.aseemsavio.blog.pojos.SignUpResponse;
import com.aseemsavio.blog.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This class acts as the entry point to the end points
 * @author Aseem Savio
 */

@RestController
public class BlogController {

    @Autowired
    AuthService authService;

    @PutMapping(value = {"/user", "signUp"})
    public SignUpResponse signUp(@RequestBody SignUpRequest signUpRequest) throws SanityCheckFailedException, UserAlreadyFoundException {
        return authService.signUp(signUpRequest);
    }

    @PostMapping(value = {"/user", "signIn"})
    public SignUpResponse signIn(@RequestBody SignInRequest signInRequest) throws UserNotFoundException, SanityCheckFailedException {
        return authService.signIn(signInRequest);
    }

}

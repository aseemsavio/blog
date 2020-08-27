package com.aseemsavio.blog.controllers;

import com.aseemsavio.blog.exceptions.DatabaseException;
import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.exceptions.UserAlreadyFoundException;
import com.aseemsavio.blog.exceptions.UserNotFoundException;
import com.aseemsavio.blog.pojos.*;
import com.aseemsavio.blog.services.AuthService;
import com.aseemsavio.blog.services.PostService;
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
    
    @Autowired
    PostService postService;

    @PutMapping(value = {"/user", "signUp"})
    public SignUpResponse signUp(@RequestBody SignUpRequest signUpRequest) throws SanityCheckFailedException, UserAlreadyFoundException {
        return authService.signUp(signUpRequest);
    }

    @PostMapping(value = {"/user", "signIn"})
    public SignUpResponse signIn(@RequestBody SignInRequest signInRequest) throws UserNotFoundException, SanityCheckFailedException {
        return authService.signIn(signInRequest);
    }

    @GetMapping("/me/{accessToken}")
    public User getMyInfo(@PathVariable("accessToken") String accessToken) throws UserNotFoundException, SanityCheckFailedException {
        return authService.getMyInfo(accessToken);
    }

    @PutMapping("/post")
    public Post createPost(@RequestBody CreatePostRequest createPostRequest) throws DatabaseException, SanityCheckFailedException {
        return postService.createPost(createPostRequest);
    }

}

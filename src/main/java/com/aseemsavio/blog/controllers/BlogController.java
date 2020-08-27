package com.aseemsavio.blog.controllers;

import com.aseemsavio.blog.exceptions.*;
import com.aseemsavio.blog.pojos.*;
import com.aseemsavio.blog.services.AuthService;
import com.aseemsavio.blog.services.CommentService;
import com.aseemsavio.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.aseemsavio.blog.utils.BlogConstants.ACCESS_TOKEN_HEADER;

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
    
    @Autowired
    CommentService commentService;

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

    @PutMapping("/secure/post")
    public Post createPost(@RequestBody CreatePostRequest createPostRequest, @RequestHeader(ACCESS_TOKEN_HEADER) String accessToken) throws DatabaseException, SanityCheckFailedException, UserNotFoundException {
        return postService.createPost(createPostRequest, accessToken);
    }

    @GetMapping("/posts")
    public List<String> listAllBlogs() throws PostNotFoundException {
        return postService.getAllBlogTitles();
    }

    @PutMapping("/secure/comment")
    public Comment addComment(@RequestBody CreateCommentRequest createCommentRequest, @RequestHeader(ACCESS_TOKEN_HEADER) String accessToken) throws UserNotFoundException, PostNotFoundException, SanityCheckFailedException, CommentNotFoundException {
        return commentService.addComment(createCommentRequest, accessToken);
    }

    @GetMapping("/secure/post/{postId}")
    public PostDetail getPostDetail(@PathVariable("postId") String postId) throws PostNotFoundException {
        return postService.getPostDetails(postId);
    }

    @PutMapping("/secure/post/{postId}")
    public PostDetail updatePost( @RequestBody CreatePostRequest updatePostRequest, @RequestHeader(ACCESS_TOKEN_HEADER) String accessToken, @PathVariable("postId") String postId) throws PostNotFoundException, SanityCheckFailedException, UserNotFoundException {
        return postService.updatePost(updatePostRequest, accessToken, postId);
    }
    
    @DeleteMapping("secure/post/{postId}")
    public GenericBlogResponse deletePost(@RequestHeader(ACCESS_TOKEN_HEADER) String accessToken, @PathVariable("postId") String postId) throws UserNotFoundException {
        return postService.deletePost(accessToken, postId);
    }

}

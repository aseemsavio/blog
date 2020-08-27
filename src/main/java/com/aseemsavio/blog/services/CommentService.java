package com.aseemsavio.blog.services;

import com.aseemsavio.blog.exceptions.CommentNotFoundException;
import com.aseemsavio.blog.exceptions.PostNotFoundException;
import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.exceptions.UserNotFoundException;
import com.aseemsavio.blog.pojos.Comment;
import com.aseemsavio.blog.pojos.CreateCommentRequest;
import com.aseemsavio.blog.pojos.Post;
import com.aseemsavio.blog.pojos.User;
import com.aseemsavio.blog.repositories.CommentRepository;
import com.aseemsavio.blog.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;
    
    @Autowired
    AuthService authService;
    
    @Autowired
    PostService postService;

    /**
     * Adds a comment to an existing post
     * @param createCommentRequest
     * @param accessToken
     * @return
     * @throws UserNotFoundException
     * @throws PostNotFoundException
     * @throws SanityCheckFailedException
     * @throws CommentNotFoundException
     */
    public Comment addComment(CreateCommentRequest createCommentRequest, String accessToken) throws UserNotFoundException, PostNotFoundException, SanityCheckFailedException, CommentNotFoundException {
        User user = authService.findUserByAccessToken(accessToken);
        String userName = user.getUserName();
        
        if(sanityCheckPassesForCommentCreation(createCommentRequest)) {
            Post post = postService.getPostByPostId(createCommentRequest.getPostId());
            if(null != post) {
                Comment comment = new Comment();
                comment.setCommentorUserId(userName);
                comment.setComment(createCommentRequest.getComment());
                comment.setLocalDateTime(LocalDateTime.now());
                comment.setPostId(post.getPostId());
                try {
                    Comment savedComment = commentRepository.save(comment);
                    Post withAddedComment = postService.addCommentToPost(post, savedComment);
                    if(null == savedComment || null == withAddedComment)
                        throw new CommentNotFoundException();
                    else {
                        return savedComment;
                    }
                } catch (Exception exception) {
                    throw new CommentNotFoundException();
                }
            } else
                throw new PostNotFoundException();
        } else {
            throw new SanityCheckFailedException();
        }
    }
    
    public List<Comment> getAllCommentsById(List<String> commentIds) {
        try {
            List<Comment> comments = (List<Comment>) commentRepository.findAllById(commentIds);
            if(comments == null)
                return null;
            else
                return comments;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean sanityCheckPassesForCommentCreation(CreateCommentRequest createCommentRequest) {
        return !StringUtils.isNullOrEmpty(createCommentRequest.getPostId()) && !StringUtils.isNullOrEmpty(createCommentRequest.getComment());
    }
}

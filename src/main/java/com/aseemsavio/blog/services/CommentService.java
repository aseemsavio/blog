package com.aseemsavio.blog.services;

import com.aseemsavio.blog.exceptions.CommentNotFoundException;
import com.aseemsavio.blog.exceptions.PostNotFoundException;
import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.exceptions.UserNotFoundException;
import com.aseemsavio.blog.pojos.*;
import com.aseemsavio.blog.repositories.CommentRepository;
import com.aseemsavio.blog.utils.BlogConstants;
import com.aseemsavio.blog.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.aseemsavio.blog.utils.BlogConstants.*;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AuthService authService;

    @Autowired
    PostService postService;

    @Autowired
    MongoOperations mongoOperations;

    /**
     * Adds a comment to an existing post
     *
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

        if (sanityCheckPassesForCommentCreation(createCommentRequest)) {
            Post post = postService.getPostByPostId(createCommentRequest.getPostId());
            if (null != post) {
                Comment comment = new Comment();
                comment.setCommentorUserId(userName);
                comment.setComment(createCommentRequest.getComment());
                comment.setLocalDateTime(LocalDateTime.now());
                comment.setPostId(post.getPostId());
                try {
                    Comment savedComment = commentRepository.save(comment);
                    Post withAddedComment = postService.addCommentToPost(post, savedComment);
                    if (null == savedComment || null == withAddedComment)
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

    /**
     * Get all comments by ID
     *
     * @param commentIds
     * @return
     */
    public List<Comment> getAllCommentsById(List<String> commentIds) {
        try {
            List<Comment> comments = (List<Comment>) commentRepository.findAllById(commentIds);
            return comments;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean sanityCheckPassesForCommentCreation(CreateCommentRequest createCommentRequest) {
        return !StringUtils.isNullOrEmpty(createCommentRequest.getPostId()) && !StringUtils.isNullOrEmpty(createCommentRequest.getComment());
    }

    /**
     * Update comment
     *
     * @param accessToken
     * @param commentId
     * @param editComment
     * @return
     * @throws UserNotFoundException
     * @throws CommentNotFoundException
     */
    public Comment updateComment(String accessToken, String commentId, CreateCommentRequest editComment) throws UserNotFoundException, CommentNotFoundException {
        User user = authService.findUserByAccessToken(accessToken);
        Query query = new Query();
        query.addCriteria(Criteria.where("commentId").is(commentId));
        Comment foundComment = mongoOperations.findOne(query, Comment.class);

        if (null != foundComment && foundComment.getCommentorUserId().equalsIgnoreCase(user.getUserName())) {
            foundComment.setComment(editComment.getComment());
            Comment editedComment = mongoOperations.save(foundComment);
            if (null != editedComment) return editedComment;
            else throw new CommentNotFoundException();
        } else throw new CommentNotFoundException();
    }

    /**
     * Delete Comment
     *
     * @param accessToken
     * @param commentId
     * @return
     * @throws UserNotFoundException
     */
    public GenericBlogResponse deleteComment(String accessToken, String commentId) throws UserNotFoundException {
        User user = authService.findUserByAccessToken(accessToken);
        Comment foundComment = commentRepository.findByCommentId(commentId);
        if (null != foundComment && foundComment.getCommentorUserId().equalsIgnoreCase(user.getUserName())) {
            commentRepository.delete(foundComment);
            return new GenericBlogResponse(SUCCESS, SUCCESS_MESSAGE);
        } else return new GenericBlogResponse(FAILURE, FAILURE_MESSAGE);
    }
}

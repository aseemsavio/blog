package com.aseemsavio.blog.services;

import com.aseemsavio.blog.exceptions.DatabaseException;
import com.aseemsavio.blog.exceptions.PostNotFoundException;
import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.exceptions.UserNotFoundException;
import com.aseemsavio.blog.pojos.*;
import com.aseemsavio.blog.repositories.CommentRepository;
import com.aseemsavio.blog.repositories.PostRepository;
import com.aseemsavio.blog.utils.BlogConstants;
import com.aseemsavio.blog.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.aseemsavio.blog.utils.BlogConstants.*;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    CommentService commentService;

    @Autowired
    AuthService authService;

    /**
     * Creates a post
     *
     * @param createPostRequest
     * @param accessToken
     * @return
     * @throws DatabaseException
     * @throws SanityCheckFailedException
     */
    public Post createPost(CreatePostRequest createPostRequest, String accessToken) throws DatabaseException, SanityCheckFailedException, UserNotFoundException {
        if (sanityCheckPassedForCreatePost(createPostRequest)) {
            String userName = authService.findUserByAccessToken(accessToken).getUserName();
            Post post = new Post();
            post.setTitle(createPostRequest.getTitle());
            post.setDescription(createPostRequest.getDescription());
            post.setHtmlContent(createPostRequest.getHtmlContent());
            post.setCreationTimeStamp(LocalDateTime.now());
            post.setCreatorUserName(userName);
            try {
                Post createdUser = postRepository.save(post);
                if (null != createdUser)
                    return createdUser;
            } catch (Exception exception) {
                throw new DatabaseException();
            }
        }
        throw new SanityCheckFailedException();
    }

    /**
     * Sanity Check for Post Creation
     *
     * @param createPostRequest
     * @return
     */
    private boolean sanityCheckPassedForCreatePost(CreatePostRequest createPostRequest) {
        return !StringUtils.isNullOrEmpty(createPostRequest.getTitle())
                && !StringUtils.isNullOrEmpty(createPostRequest.getDescription())
                && !StringUtils.isNullOrEmpty(createPostRequest.getHtmlContent());
    }

    /**
     * Get All posts
     *
     * @return
     * @throws PostNotFoundException
     */
    public List<String> getAllBlogTitles() throws PostNotFoundException {
        try {
            List<String> posts = postRepository.findAll()
                    .stream()
                    .map(post -> post.getTitle().substring(0, 301))
                    .collect(Collectors.toList());
            if (null == posts)
                throw new PostNotFoundException();
            else
                return posts;
        } catch (Exception exception) {
            throw new PostNotFoundException();
        }
    }

    /**
     * Gets Post by ID
     *
     * @param postId
     * @return
     * @throws PostNotFoundException
     */
    public Post getPostByPostId(String postId) throws PostNotFoundException {
        try {
            Post post = postRepository.findByPostId(postId);
            if (null == post)
                throw new PostNotFoundException();
            return post;

        } catch (Exception exception) {
            throw new PostNotFoundException();
        }
    }


    /**
     * Gets Post Details by ID
     *
     * @param postId
     * @return
     */
    public PostDetail getPostDetails(String postId) throws PostNotFoundException {
        try {
            Post post = getPostByPostId(postId);
            List<Comment> comments = commentService.getAllCommentsById(post.getCommentIds());
            PostDetail postDetail = new PostDetail();
            postDetail.setPostId(post.getPostId());
            postDetail.setTitle(post.getTitle());
            postDetail.setCreationTimeStamp(post.getCreationTimeStamp());
            postDetail.setDescription(post.getDescription());
            postDetail.setHtmlContent(post.getHtmlContent());
            postDetail.setComments(comments);
            postDetail.setLikesUserIds(post.getLikesUserIds());
            postDetail.setCreatorUserName(post.getCreatorUserName());
            return postDetail;
        } catch (Exception exception) {
            throw new PostNotFoundException();
        }
    }

    /**
     * Adds comments to posts
     *
     * @param post
     * @param savedComment
     * @return
     */
    public Post addCommentToPost(Post post, Comment savedComment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("postId").is(post.getPostId()));
        Post foundPost = mongoOperations.findOne(query, Post.class);
        List<String> commentators = foundPost.getCommentIds();
        if (null == commentators) {
            foundPost.setCommentIds(List.of(savedComment.getCommentId()));
            return mongoOperations.save(foundPost);
        } else {
            commentators.add(savedComment.getCommentId());
            foundPost.setCommentIds(commentators);
            return mongoOperations.save(foundPost);
        }
    }

    /**
     * Updates Posts
     *
     * @param updatePostRequest
     * @param accessToken
     * @param postId
     * @return
     * @throws UserNotFoundException
     * @throws SanityCheckFailedException
     * @throws PostNotFoundException
     */
    public PostDetail updatePost(CreatePostRequest updatePostRequest, String accessToken, String postId) throws UserNotFoundException, SanityCheckFailedException, PostNotFoundException {

        if (sanityCheckPassedForCreatePost(updatePostRequest)) {
            String userName = authService.findUserByAccessToken(accessToken).getUserName();
            Post post = postRepository.findByPostId(postId);
            if (null == post) throw new PostNotFoundException();
            else {
                if (post.getCreatorUserName().equalsIgnoreCase(userName)) {
                    Query query = new Query();
                    query.addCriteria(Criteria.where("postId").is(postId));
                    Post foundPost = mongoOperations.findOne(query, Post.class);
                    if (null != foundPost) {
                        foundPost.setTitle(updatePostRequest.getTitle());
                        foundPost.setHtmlContent(updatePostRequest.getHtmlContent());
                        foundPost.setDescription(updatePostRequest.getDescription());
                        Post updatedPost = mongoOperations.save(foundPost);
                        return translateToPostDetail(updatedPost);

                    } else throw new PostNotFoundException();

                } else throw new PostNotFoundException();
            }

        } else throw new SanityCheckFailedException();

    }

    private PostDetail translateToPostDetail(Post post) {
        PostDetail postDetail = new PostDetail();
        postDetail.setPostId(post.getPostId());
        postDetail.setTitle(post.getTitle());
        postDetail.setCreationTimeStamp(post.getCreationTimeStamp());
        postDetail.setDescription(post.getDescription());
        postDetail.setHtmlContent(post.getHtmlContent());

        List<Comment> comments = commentService.getAllCommentsById(post.getCommentIds());
        postDetail.setComments(comments);
        postDetail.setLikesUserIds(post.getLikesUserIds());
        postDetail.setCreatorUserName(post.getCreatorUserName());
        return postDetail;
    }

    public GenericBlogResponse deletePost(String accessToken, String postId) throws UserNotFoundException {

        String userName = authService.findUserByAccessToken(accessToken).getUserName();
        Post post = postRepository.findByPostId(postId);

        if (null != post && post.getCreatorUserName().equalsIgnoreCase(userName)) {
            try {
                postRepository.delete(post);
                return new GenericBlogResponse(SUCCESS, SUCCESS_MESSAGE);
            } catch (Exception exception) {
                return new GenericBlogResponse(FAILURE, FAILURE_MESSAGE);
            }
        } else {
            return new GenericBlogResponse(FAILURE, FAILURE_MESSAGE);
        }

    }

    /**
     * Like a post
     *
     * @param accessToken
     * @param postId
     * @return
     * @throws UserNotFoundException
     */
    public GenericBlogResponse likePost(String accessToken, String postId) throws UserNotFoundException {
        String userName = authService.findUserByAccessToken(accessToken).getUserName();
        Query query = new Query();
        query.addCriteria(Criteria.where("postId").is(postId));
        Post foundPost = mongoOperations.findOne(query, Post.class);
        List<String> likers = foundPost.getLikesUserIds();
        try {
            if (null == likers) {
                foundPost.setLikesUserIds(List.of(userName));
                mongoOperations.save(foundPost);
                return new GenericBlogResponse(SUCCESS, SUCCESS_MESSAGE);
            } else {
                if (!likers.contains(userName)) {
                    likers.add(userName);
                    foundPost.setLikesUserIds(likers);
                    mongoOperations.save(foundPost);
                    return new GenericBlogResponse(SUCCESS, SUCCESS_MESSAGE);
                } else return new GenericBlogResponse(FAILURE, FAILURE_MESSAGE);
            }
        } catch (Exception exception) {
            return new GenericBlogResponse(FAILURE, FAILURE_MESSAGE);
        }
    }
}

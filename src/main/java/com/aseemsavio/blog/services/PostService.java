package com.aseemsavio.blog.services;

import com.aseemsavio.blog.exceptions.DatabaseException;
import com.aseemsavio.blog.exceptions.PostNotFoundException;
import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.pojos.CreatePostRequest;
import com.aseemsavio.blog.pojos.Post;
import com.aseemsavio.blog.repositories.PostRepository;
import com.aseemsavio.blog.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    /**
     * Creates a post
     *
     * @param createPostRequest
     * @return
     * @throws DatabaseException
     * @throws SanityCheckFailedException
     */
    public Post createPost(CreatePostRequest createPostRequest) throws DatabaseException, SanityCheckFailedException {
        if (sanityCheckPassedForCreatePost(createPostRequest)) {
            Post post = new Post();
            post.setTitle(createPostRequest.getTitle());
            post.setDescription(createPostRequest.getDescription());
            post.setHtmlContent(createPostRequest.getHtmlContent());
            post.setCreationTimeStamp(LocalDateTime.now());
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
}

package com.aseemsavio.blog.services;

import com.aseemsavio.blog.exceptions.DatabaseException;
import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.pojos.CreatePostRequest;
import com.aseemsavio.blog.pojos.Post;
import com.aseemsavio.blog.repositories.PostRepository;
import com.aseemsavio.blog.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

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

    private boolean sanityCheckPassedForCreatePost(CreatePostRequest createPostRequest) {
        return !StringUtils.isNullOrEmpty(createPostRequest.getTitle())
                && !StringUtils.isNullOrEmpty(createPostRequest.getDescription())
                && !StringUtils.isNullOrEmpty(createPostRequest.getHtmlContent());
    }
}

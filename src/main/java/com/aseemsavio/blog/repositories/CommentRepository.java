package com.aseemsavio.blog.repositories;

import com.aseemsavio.blog.pojos.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Comment findByCommentId(String commentId);
}

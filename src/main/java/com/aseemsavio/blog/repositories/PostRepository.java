package com.aseemsavio.blog.repositories;

import com.aseemsavio.blog.pojos.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}

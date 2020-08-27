package com.aseemsavio.blog.repositories;

import com.aseemsavio.blog.pojos.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(String userName);

    User findByUserNameAndPassword(String userName, String password);

    User findByAccessToken(String accessToken);
}

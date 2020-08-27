package com.aseemsavio.blog.services;

import com.aseemsavio.blog.exceptions.SanityCheckFailedException;
import com.aseemsavio.blog.exceptions.UserAlreadyFoundException;
import com.aseemsavio.blog.exceptions.UserNotFoundException;
import com.aseemsavio.blog.pojos.SignInRequest;
import com.aseemsavio.blog.pojos.SignUpRequest;
import com.aseemsavio.blog.pojos.SignUpResponse;
import com.aseemsavio.blog.pojos.User;
import com.aseemsavio.blog.repositories.UserRepository;
import com.aseemsavio.blog.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for Authentication
 *
 * @author Aseem Savio
 */

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    /**
     * This method signs up users.
     *
     * @param signUpRequest
     * @return
     * @throws UserAlreadyFoundException
     * @throws SanityCheckFailedException
     */
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyFoundException, SanityCheckFailedException {
        if (sanityCheckPasses(signUpRequest)) {
            if (null == userRepository.findByUserName(signUpRequest.getUserName())) {
                User user = new User();
                user.setUserName(signUpRequest.getUserName());
                user.setPassword(signUpRequest.getPassword());
                user.setName(signUpRequest.getName());
                String accessToken = userRepository.save(user).getAccessToken();
                logger.info("User added: " + accessToken);
                return new SignUpResponse(accessToken);
            } else
                throw new UserAlreadyFoundException();
        }
        throw new SanityCheckFailedException();
    }

    /**
     * Sign In to get Access Token
     *
     * @param signInRequest
     * @return
     * @throws UserNotFoundException
     * @throws SanityCheckFailedException
     */
    public SignUpResponse signIn(SignInRequest signInRequest) throws UserNotFoundException, SanityCheckFailedException {
        if (sanityCheckPassesForSignIn(signInRequest)) {
            User user = userRepository.findByUserNameAndPassword(signInRequest.getUserName(), signInRequest.getPassword());
            if (null != user && !StringUtils.isNullOrEmpty(user.getAccessToken()))
                return new SignUpResponse(user.getAccessToken());
            else
                throw new UserNotFoundException();
        }
        throw new SanityCheckFailedException();
    }

    /**
     * Get My Info
     *
     * @param accessToken
     * @return
     * @throws UserNotFoundException
     * @throws SanityCheckFailedException
     */
    public User getMyInfo(String accessToken) throws UserNotFoundException, SanityCheckFailedException {
        if (!StringUtils.isNullOrEmpty(accessToken)) {
            User user = userRepository.findByAccessToken(accessToken);
            if (null != user) {
                user.setPassword(null);
                return user;
            } else
                throw new UserNotFoundException();
        }
        throw new SanityCheckFailedException();
    }

    /**
     * Sanity Check for Sign Up requests
     *
     * @param signUpRequest
     * @return
     */
    private boolean sanityCheckPasses(SignUpRequest signUpRequest) {
        return !StringUtils.isNullOrEmpty(signUpRequest.getUserName())
                && !StringUtils.isNullOrEmpty(signUpRequest.getPassword())
                && !StringUtils.isNullOrEmpty(signUpRequest.getName());
    }

    private boolean sanityCheckPassesForSignIn(SignInRequest signInRequest) {
        return !StringUtils.isNullOrEmpty(signInRequest.getUserName()) && !StringUtils.isNullOrEmpty(signInRequest.getPassword());
    }

}

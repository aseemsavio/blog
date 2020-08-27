package com.aseemsavio.blog.pojos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comment")
public class Comment {

    @Id
    private String commentId;
    private String comment;
    private String postId;
    private String commentorUserId;
    private LocalDateTime localDateTime;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentorUserId() {
        return commentorUserId;
    }

    public void setCommentorUserId(String commentorUserId) {
        this.commentorUserId = commentorUserId;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", comment='" + comment + '\'' +
                ", postId='" + postId + '\'' +
                ", commentorUserId='" + commentorUserId + '\'' +
                ", localDateTime=" + localDateTime +
                '}';
    }
}

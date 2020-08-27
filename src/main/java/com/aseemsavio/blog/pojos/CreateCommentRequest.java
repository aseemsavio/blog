package com.aseemsavio.blog.pojos;

public class CreateCommentRequest {

    private String comment;
    private String postId;

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

    @Override
    public String toString() {
        return "CreateCommentRequest{" +
                "comment='" + comment + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}

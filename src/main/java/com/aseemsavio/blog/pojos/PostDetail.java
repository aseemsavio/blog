package com.aseemsavio.blog.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetail {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String postId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String htmlContent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Comment> comments;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> likesUserIds;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime creationTimeStamp;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getLikesUserIds() {
        return likesUserIds;
    }

    public void setLikesUserIds(List<String> likesUserIds) {
        this.likesUserIds = likesUserIds;
    }

    public LocalDateTime getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    @Override
    public String toString() {
        return "PostDetail{" +
                "postId='" + postId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", comments=" + comments +
                ", likesUserIds=" + likesUserIds +
                ", creationTimeStamp=" + creationTimeStamp +
                '}';
    }
}

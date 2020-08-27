package com.aseemsavio.blog.pojos;

public class CreatePostRequest {

    private String title;
    private String description;
    private String htmlContent;

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

    @Override
    public String toString() {
        return "CreatePost{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                '}';
    }
}

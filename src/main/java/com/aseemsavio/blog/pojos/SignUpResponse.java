package com.aseemsavio.blog.pojos;

public class SignUpResponse {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public SignUpResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "SignUpResponse{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}

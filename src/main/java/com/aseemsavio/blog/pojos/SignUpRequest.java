package com.aseemsavio.blog.pojos;

/**
 * Request Model for Sign Up
 *
 * @author Aseem Savio
 */

public class SignUpRequest {

    private String userName;
    private String password;
    private String Name;

    public SignUpRequest() {
    }

    public SignUpRequest(String userName, String password, String name) {
        this.userName = userName;
        this.password = password;
        Name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}

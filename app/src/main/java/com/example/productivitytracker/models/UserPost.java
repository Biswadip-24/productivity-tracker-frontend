package com.example.productivitytracker.models;

public class UserPost {
    public int postId;
    public String body;
    public String timestamp;
    public int numLikes;
    public int numComments;
    public int user;

    private String userName;
    private String userImage;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

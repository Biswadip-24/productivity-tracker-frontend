package com.example.productivitytracker.models;

import java.util.List;

public class User {
    public int userID;
    public String email;
    public String userName;
    public String firstName;
    public String lastName;

    public boolean isNewUser;
    public List<Float> lastWeekProductivity;
    public List<String> post_likes;
    public List<Integer> badges;
}

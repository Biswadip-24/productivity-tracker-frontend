package com.example.productivitytracker.models;

import java.util.List;

public class User {
    int userID;
    String email;
    String userName;
    String firstName;
    String lastName;

    boolean isNewUser;
    List<Double> lastWeekProductivity;
    List<Integer> post_likes;
}

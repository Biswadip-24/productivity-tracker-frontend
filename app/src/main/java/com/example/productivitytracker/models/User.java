package com.example.productivitytracker.models;

import java.util.List;

public class User {
    String userID;
    String email;
    String userName;
    String firstName;
    String lastName;

    boolean isNewUser;
    List<Double> lastWeekProductivity;
    int followers;

}

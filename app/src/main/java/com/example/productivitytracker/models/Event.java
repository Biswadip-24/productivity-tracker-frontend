package com.example.productivitytracker.models;

import java.util.List;

public class Event {
    public int eventID;
    public String title;
    public String description;
    public long start_time;
    public long end_time;
    public String date;
    public String type;
    public List<Integer> users;
}

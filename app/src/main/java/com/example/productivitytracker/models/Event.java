package com.example.productivitytracker.models;

import java.util.List;

public class Event {
    String name;
    String icon;
    float time;
    double percentage;

    public int eventID;
    public String title;
    public String description;
    public String start_time;
    public String end_time;
    public String date;
    public List<String> type;
    public List<String> users;

    public Event(String name, String icon, float time, double percentage) {
        this.name = name;
        this.icon = icon;
        this.time = time;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public float getTime() {
        return time;
    }

    public double getPercentage() {
        return percentage;
    }
}

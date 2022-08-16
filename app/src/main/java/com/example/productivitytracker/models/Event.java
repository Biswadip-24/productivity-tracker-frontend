package com.example.productivitytracker.models;

public class Event {
    String name;
    String icon;
    float time;
    double percentage;

    String eventID;
    String title;
    String description;
    String start_time;
    String end_time;
    String date;

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

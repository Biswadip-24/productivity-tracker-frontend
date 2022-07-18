package com.example.productivitytracker.models;

public class Activity {
    String name;
    String icon;
    float time;
    double percentage;

    public Activity(String name, String icon, float time, double percentage) {
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

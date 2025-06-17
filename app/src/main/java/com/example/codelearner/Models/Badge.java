package com.example.codelearner.Models;

public class Badge {
    private String title;
    private String description;
    private boolean earned;

    public Badge(String title, String description, boolean earned) {
        this.title = title;
        this.description = description;
        this.earned = earned;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isEarned() { return earned; }
}

package com.example.codelearner.Models;

import java.util.List;

public class LearningPath {
    private String id;
    private String studentId;
    private String careerGoal;
    private int weeksPlanned;
    private List<String> topics;
    private List<String> completedTopics;
    private String badge;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCareerGoal() { return careerGoal; }
    public void setCareerGoal(String careerGoal) { this.careerGoal = careerGoal; }

    public int getWeeksPlanned() { return weeksPlanned; }
    public void setWeeksPlanned(int weeksPlanned) { this.weeksPlanned = weeksPlanned; }

    public List<String> getTopics() { return topics; }
    public void setTopics(List<String> topics) { this.topics = topics; }

    public List<String> getCompletedTopics() { return completedTopics; }
    public void setCompletedTopics(List<String> completedTopics) { this.completedTopics = completedTopics; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
}

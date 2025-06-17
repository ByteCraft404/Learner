package com.example.codelearner.Models;

import java.util.List;

public class LearningPlanRequest {
    private String careerGoal;
    private List<String> grades;
    private int hoursPerWeek;
    private String learningStyle;


    public LearningPlanRequest(String careerGoal, List<String> grades, int hoursPerWeek, String learningStyle) {
        this.careerGoal = careerGoal;
        this.grades = grades;
        this.hoursPerWeek = hoursPerWeek;
        this.learningStyle = learningStyle;
    }

    // ✅ Getters & Setters
    public String getCareerGoal() { return careerGoal; }
    public void setCareerGoal(String careerGoal) { this.careerGoal = careerGoal; }

    public List<String> getGrades() { return grades; }
    public void setGrades(List<String> grades) { this.grades = grades; }

    public int getHoursPerWeek() { return hoursPerWeek; }
    public void setHoursPerWeek(int hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }

    public String getLearningStyle() { return learningStyle; }
    public void setLearningStyle(String learningStyle) { this.learningStyle = learningStyle; }
}

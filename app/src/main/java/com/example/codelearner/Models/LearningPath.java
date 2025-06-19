package com.example.codelearner.Models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class LearningPath implements Parcelable {
    private String id;
    private String studentId;
    private String careerGoal;
    private int weeksPlanned;
    private List<String> topics;
    private List<String> resources; // Added missing resources field
    private List<String> completedTopics;
    private String badge;

    // Default constructor (required for Parcelable)
    public LearningPath() {
    }

    // Constructor to create object from data
    public LearningPath(String id, String studentId, String careerGoal, int weeksPlanned,
                        List<String> topics, List<String> resources, List<String> completedTopics,
                        String badge) {
        this.id = id;
        this.studentId = studentId;
        this.careerGoal = careerGoal;
        this.weeksPlanned = weeksPlanned;
        this.topics = topics;
        this.resources = resources;
        this.completedTopics = completedTopics;
        this.badge = badge;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getCareerGoal() { return careerGoal; }
    public int getWeeksPlanned() { return weeksPlanned; }
    public List<String> getTopics() { return topics; }
    public List<String> getResources() { return resources; } // Getter for resources
    public List<String> getCompletedTopics() { return completedTopics; }
    public String getBadge() { return badge; }

    // --- SETTERS ---
    public void setId(String id) { this.id = id; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setCareerGoal(String careerGoal) { this.careerGoal = careerGoal; }
    public void setWeeksPlanned(int weeksPlanned) { this.weeksPlanned = weeksPlanned; }
    public void setTopics(List<String> topics) { this.topics = topics; }
    public void setResources(List<String> resources) { this.resources = resources; } // Setter for resources
    public void setCompletedTopics(List<String> completedTopics) { this.completedTopics = completedTopics; }
    public void setBadge(String badge) { this.badge = badge; }


    // --- PARCELABLE IMPLEMENTATION ---

    protected LearningPath(Parcel in) {
        id = in.readString();
        studentId = in.readString();
        careerGoal = in.readString();
        weeksPlanned = in.readInt();
        topics = in.createStringArrayList();
        resources = in.createStringArrayList(); // Read resources list
        completedTopics = in.createStringArrayList();
        badge = in.readString();
    }

    public static final Creator<LearningPath> CREATOR = new Creator<LearningPath>() {
        @Override
        public LearningPath createFromParcel(Parcel in) {
            return new LearningPath(in);
        }

        @Override
        public LearningPath[] newArray(int size) {
            return new LearningPath[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(studentId);
        dest.writeString(careerGoal);
        dest.writeInt(weeksPlanned);
        dest.writeStringList(topics);
        dest.writeStringList(resources); // Write resources list
        dest.writeStringList(completedTopics);
        dest.writeString(badge);
    }
}

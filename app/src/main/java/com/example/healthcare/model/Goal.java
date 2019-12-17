package com.example.healthcare.model;

public class Goal {

    private String date;
    private String goal;
    private String time;

    public Goal() {
    }

    public Goal(String date, String goal) {
        this.date = date;
        this.goal = goal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

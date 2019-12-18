package com.example.healthcare.model;

public class BloodSugar {

    private int concentrationSugar;
    private String measured;
    private String date;
    private String time;
    private String notes;
    private String tag;

    public int getConcentrationSugar() {
        return concentrationSugar;
    }

    public void setConcentrationSugar(int concentrationSugar) {
        this.concentrationSugar = concentrationSugar;
    }

    public String getMeasured() {
        return measured;
    }

    public void setMeasured(String measured) {
        this.measured = measured;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



}

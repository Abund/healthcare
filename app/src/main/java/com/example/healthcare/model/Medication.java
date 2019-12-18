package com.example.healthcare.model;

public class Medication {

    private String medicationName;
    private String starteDate;
    private String instructions;
    private String Units;
    private String endDate;
    private String numberOfDays;
    private String notes;
    private String repeats;

    public Medication() {
    }

    public Medication(String medicationName, String starteDate, String instructions, String units, String endDate, String numberOfDays, String notes, String repeats) {
        this.medicationName = medicationName;
        this.starteDate = starteDate;
        this.instructions = instructions;
        this.Units = units;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.notes = notes;
        this.repeats = repeats;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getStarteDate() {
        return starteDate;
    }

    public void setStarteDate(String starteDate) {
        this.starteDate = starteDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getUnits() {
        return Units;
    }

    public void setUnits(String units) {
        Units = units;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRepeats() {
        return repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }
}

package com.example.myapplication;

public class SummaryObject {

    private String disabilityType;
    private int rating;
    private String date;

    public SummaryObject(String disabilityType, int rating, String date) {
        this.disabilityType = disabilityType;
        this.rating = rating;
        this.date = date;
    }

    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisabilityType() {
        return disabilityType;
    }


}

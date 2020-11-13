package com.example.myapplication;

import java.io.Serializable;

public class SummaryObject implements Serializable {

    private long id;
    private String disabilityType;
    private int rating;
    private String date;

    public SummaryObject(String disabilityType, int rating, String date) {
        this.disabilityType = disabilityType;
        this.rating = rating;
        this.date = date;
    }

    public void setId(long id) { this.id = id; }

    public long getId() { return this.id; }

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

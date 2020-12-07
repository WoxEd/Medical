package com.example.myapplication;

import java.io.Serializable;

/**
 * Object used for storing a Entry which is used to populate list of entries and for summary view
 */
public class SummaryObject implements Serializable {

    /**
     * Id of summary entry
     */
    private long id;

    /**
     * Disability type of summary
     */
    private String disabilityType;

    /**
     * Rating of summary
     */
    private int rating;

    /**
     * Date of summary
     */
    private String date;

    /**
     * Constructor initializes 4 parameters
     * @param id number
     * @param disabilityType String
     * @param rating number
     * @param date String
     */
    public SummaryObject(long id, String disabilityType, int rating, String date) {
        setId(id);
        setDisabilityType(disabilityType);
        setRating(rating);
        setDate(date);
    }

    /**
     * Mutator for id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Accessor for id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Mutator for disability type
     */
    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    /**
     * Accessor for rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Mutator for rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Accessor for id
     */
    public String getDate() {
        return date;
    }

    /**
     * Mutator for date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Accessor for Disability Type
     */
    public String getDisabilityType() {
        return disabilityType;
    }


}

package com.example.myapplication;

/**
 * The Object responsible for holding attributes of a profile.
 * This object is used to populate a list which holds profile details.
 */
public class Profile {
    /**
     * Numeric id of the profile
     */
    private long id;

    /**
     * First name of the profile
     */
    private String firstName;

    /**
     * Last name of the profile
     */
    private String lastName;

    /**
     * Three arg constructor which initializes id, first name, and last name
     */
    public Profile(long id, String firstName, String lastName) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
    }

    /**
     * Accessor for id
     */
    public long getId() {
        return id;
    }

    /**
     * Mutator for id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Accessor for first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Mutator for id
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Accessor for last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Mutator for id
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

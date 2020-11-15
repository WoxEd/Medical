package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProfileListActivity extends AppCompatActivity {

    /**
     * The stored profiles list (NEED TO BE UPDATED W/ DATABASE)
     */
    public static final String SELECTED_FIRST = "FIRST";
    public static final String SELECTED_LAST = "LAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
    }
}
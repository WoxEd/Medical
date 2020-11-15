package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /**
     * public Strings to make it easier to fetch intents from Questions page
     */
    public static final String DISABILITY_TYPE = "DISABILITY";
    public static final String VISION = "Vision";
    public static final String SPEAKING = "Speaking";
    public static final String HEARING = "Hearing";
    public static final String WALKING  = "Walking";
    public static final String ELIMINATING = "Eliminating";
    public static final String FEEDING = "Feeding";
    public static final String DRESSING = "Dressing";
    public static final String MENTAL = "Mental";
    public static final String DATE = "Date";
    public static final String LIST = "List";

    /**
     * Intent for going to next page
     */
    private Intent goToActivity;

    /**
     * On create for Main Activity should simply initialize buttons and implement action listeners
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createEntry = findViewById(R.id.entryButton);
        Button viewIndividual = findViewById(R.id.listViewButton);
        Button backHome = findViewById(R.id.logout);

        createEntry.setOnClickListener(e -> {
            goToActivity = new Intent(MainActivity.this, DisabilitySelectionActivity.class);
            startActivity(goToActivity);
        });

        viewIndividual.setOnClickListener(e -> {
            goToActivity = new Intent(MainActivity.this,ListViewActivity.class);
            startActivity(goToActivity);
        });

        backHome.setOnClickListener(e -> {
            goToActivity = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(goToActivity);
        });
    }
}
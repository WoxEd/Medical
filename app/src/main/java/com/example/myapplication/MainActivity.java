package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

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
    public static final String USER = "User ";
    public static final String WEEK = "Week";
    public static final String MONTH = "Month";
    public static final String YEAR = "Year";
    public static final String LIST = "List";

    /**
     * Button which goes to disability selection page
     */
    private Button createEntry;

    /**
     * Button which goes to list containing individual entries
     */
    private Button viewIndividual;

    /**
     * Button which goes to page with summary of entries
     */
    private Button viewSummary;

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

        createEntry = findViewById(R.id.entryButton);
        viewIndividual = findViewById(R.id.listViewButton);
        viewSummary = findViewById(R.id.summaryButton);

        createEntry.setOnClickListener(e -> {
            goToActivity = new Intent(MainActivity.this, DisabilitySelectionActivity.class);
            startActivity(goToActivity);
        });

        viewIndividual.setOnClickListener(e -> {
            goToActivity = new Intent(MainActivity.this,ListViewActivity.class);
            startActivity(goToActivity);
        });

        viewSummary.setOnClickListener(e -> {
            goToActivity = new Intent(MainActivity.this, SummaryActivity.class);
            startActivity(goToActivity);
        });

        //Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.summaryButton_spinner);

        //Spinner click listener
        //Spinner Drop down elements
        List<String> options = new ArrayList<String>();
        options.add("Bi-Weekly Summary");
        options.add("Monthly Summary");
        options.add("Yearly Summary");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);

        //Set Drop down view resource
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter to the spinner
        spinner.setAdapter(dataAdapter);

    }


}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    /**
     * public Strings to make it easier to fetch intents from Questions page
     */
    public static final String DISABILITY_TYPE = "disabilityType";
    public static final String VISION = "vision";
    public static final String SPEAKING = "speaking";
    public static final String HEARING = "hearing";
    public static final String WALKING  = "walking";
    public static final String ELIMINATING = "eliminating";
    public static final String FEEDING = "feeding";
    public static final String DRESSING = "dressing";
    public static final String MENTAL = "mental";

    private TextView dateText;
    private String default_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateText = findViewById(R.id.dateText);
        setDefaultDate();

        Button summary = findViewById(R.id.summaryButton);
        summary.setOnClickListener( e-> {
            Intent goToSummary = new Intent(MainActivity.this, SummaryActivity.class);
            startActivity(goToSummary);
        });

        Button listButton = findViewById(R.id.listViewButton);
        listButton.setOnClickListener( e-> {
            Intent goToSummary = new Intent(MainActivity.this, ListViewActivity.class);
            startActivity(goToSummary);
        });

        //Adds the click functions for the Calendar button
        ImageButton calendar = findViewById(R.id.imagecalendar);
        calendar.setOnClickListener( e -> showCalendar());

        //Adds the click functions for the Vision button
        ImageButton vision = findViewById(R.id.imagevision);
        vision.setOnClickListener( e -> addClickFunction(VISION));

        //Adds click functions for the Speaking Button
        ImageButton speaking = findViewById(R.id.imagespeaking);
        speaking.setOnClickListener( e -> addClickFunction(SPEAKING));

        //Adds click functions for the Hearing Button
        ImageButton hearing = findViewById(R.id.imagehearing);
        hearing.setOnClickListener( e -> addClickFunction(HEARING));

        //Adds click functions for the Walking Button
        ImageButton walking = findViewById(R.id.imagewalking);
        walking.setOnClickListener( e -> addClickFunction(WALKING));

        //Adds click functions for the Eliminating Button
        ImageButton eliminating = findViewById(R.id.imageeliminating);
        eliminating.setOnClickListener( e -> addClickFunction(ELIMINATING));

        //Adds click functions for the Feeding Button
        ImageButton feeding = findViewById(R.id.imagefeeding);
        feeding.setOnClickListener( e -> addClickFunction(FEEDING));

        //Adds click functions for the Dressing Button
        ImageButton dressing = findViewById(R.id.imagedressing);
        dressing.setOnClickListener( e -> addClickFunction(DRESSING));

        //Adds click functions for the Mental Button
        ImageButton mental = findViewById(R.id.imagemental);
        mental.setOnClickListener( e -> addClickFunction(MENTAL));
    }

    /**
     * When an image button is clicked, it will pass the type in the form of a String to this method
     * It will start a new intent to the QuestionActivity while passing over the disability type clicked
     * @param type String which contains the type of disability that image button represents
     */
    public void addClickFunction(String type){
        Intent goToQuestions = new Intent(MainActivity.this,QuestionActivity.class);
        switch(type) {
            case SPEAKING:
                goToQuestions.putExtra(DISABILITY_TYPE,SPEAKING);
                goToQuestions.putExtra("id",2);
                break;
            case HEARING:
                goToQuestions.putExtra(DISABILITY_TYPE,HEARING);
                goToQuestions.putExtra("id",3);
                break;
            case WALKING:
                goToQuestions.putExtra(DISABILITY_TYPE,WALKING);
                goToQuestions.putExtra("id",4);
                break;
            case ELIMINATING:
                goToQuestions.putExtra(DISABILITY_TYPE,ELIMINATING);
                goToQuestions.putExtra("id",5);
                break;
            case FEEDING:
                goToQuestions.putExtra(DISABILITY_TYPE,FEEDING);
                goToQuestions.putExtra("id",6);
                break;
            case DRESSING:
                goToQuestions.putExtra(DISABILITY_TYPE,DRESSING);
                goToQuestions.putExtra("id",7);
                break;
            case MENTAL:
                goToQuestions.putExtra(DISABILITY_TYPE,MENTAL);
                goToQuestions.putExtra("id",8);
                break;
            default: //Vision will act as default
                goToQuestions.putExtra(DISABILITY_TYPE,VISION);
                goToQuestions.putExtra("id",1);
                break;
        }
        goToQuestions.putExtra("DATE", default_date);
        startActivity(goToQuestions);
    }

    /**
     * Sets the default date of the calendar
     */
    public void setDefaultDate() {

        final Calendar cldr = Calendar.getInstance();

        // Get the current day, month, year
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        default_date = day+"/"+month+"/"+year;
        dateText.setText(default_date);
    }


    public void showCalendar() {
        DatePickerDialog picker;
        final Calendar cldr = Calendar.getInstance();

        // Get the current day, month, year
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // Create a calendar dialog for user to select the date, today's date is the default
        picker = new DatePickerDialog(MainActivity.this, (datePicker, calYear, calMonth, calDate) -> {
            // Set the date to the user's selection
            default_date = calDate + "/" + (calMonth + 1) + "/" + calYear;
            dateText.setText(default_date);
        }, year, month, day);
        picker.show();
    }
}
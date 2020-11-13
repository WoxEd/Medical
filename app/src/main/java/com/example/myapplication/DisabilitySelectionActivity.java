package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

public class DisabilitySelectionActivity extends AppCompatActivity {

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
    public static final String DATE = "date";

    /**
     * The TextView that holds the selected date of entry
     */
    private TextView dateText;

    /**
     * The String that holds the date that will be passed to the Intent
     */
    private String selectedDate;

    /**
     * The intent that holds information to be passed to Question Activity
     */
    private Intent goToQuestions;

    /**
     * On creation the selection activity will
     * 1) Initialize calendar
     * 2) Initialize calendar TextView to display current date
     * 3) Add calendar functions
     * 4) Load the disability icons
     * 5) Give the disability icons click functions
     * 6) Initialize an intent from SelectionActivity to QuestionsActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disability_selection);

        //1) initialize calendar
        ImageButton calendar = findViewById(R.id.imagecalendar);

        //2 initialize calendar date to display current date
        dateText = findViewById(R.id.dateText);
        setDefaultDate();

        //3 add calendar functions
        calendar.setOnClickListener(e -> showCalendar());

        //4 Initialize disability icons
        ImageButton vision = findViewById(R.id.imagevision);
        ImageButton speaking = findViewById(R.id.imagespeaking);
        ImageButton hearing = findViewById(R.id.imagehearing);
        ImageButton walking = findViewById(R.id.imagewalking);
        ImageButton eliminating = findViewById(R.id.imageeliminating);
        ImageButton feeding = findViewById(R.id.imagefeeding);
        ImageButton dressing = findViewById(R.id.imagedressing);
        ImageButton mental = findViewById(R.id.imagemental);

        //5 Add click listeners to each disability icon
        vision.setOnClickListener(e -> goToQuestions(VISION));
        speaking.setOnClickListener(e -> goToQuestions(SPEAKING));
        hearing.setOnClickListener(e -> goToQuestions(HEARING));
        walking.setOnClickListener(e -> goToQuestions(WALKING));
        eliminating.setOnClickListener(e -> goToQuestions(ELIMINATING));
        feeding.setOnClickListener(e -> goToQuestions(FEEDING));
        dressing.setOnClickListener(e -> goToQuestions(DRESSING));
        mental.setOnClickListener(e -> goToQuestions(MENTAL));

        //6 Initialize Intent
        goToQuestions = new Intent(DisabilitySelectionActivity.this, QuestionActivity.class);
    }

    /**
     * Go to questions will the following values into the goToQuestions intent
     * 1) Disability Type
     * 2) Date
     * @param type represents disability type
     */
    public void goToQuestions(String type){
        goToQuestions.putExtra(DISABILITY_TYPE,type);
        goToQuestions.putExtra(DATE, selectedDate);
        Log.d("Selection: date:", selectedDate);
        startActivity(goToQuestions);
    }

    /**
     * Sets the default date in the TextView dateText to the current system date using a
     * D/M/Y format
     */
    public void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();

        // Get the current day, month, year
        // Month +1 because calendar stores months starting from 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        selectedDate = ListViewActivity.createDateString(year,month,day);
        dateText.setText(selectedDate);
    }


    /**
     * Displays the calendar
     */
    public void showCalendar() {
        DatePickerDialog picker;
        Calendar calendar = Calendar.getInstance();
        // Get the current day, month, year
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Create a calendar dialog for user to select the date, today's date is the default
        picker = new DatePickerDialog(DisabilitySelectionActivity.this, (datePicker, calYear, calMonth, calDate) -> {
            // Set the date to the user's selection
            selectedDate = calDate + "/" + (calMonth + 1) + "/" + calYear;
            dateText.setText(selectedDate);
        }, year, month, day);
        picker.show();
    }
}
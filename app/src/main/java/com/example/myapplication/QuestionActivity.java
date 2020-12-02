package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;


public class QuestionActivity extends AppCompatActivity {

    /**
     * String which will hold the name of the disability the page is based on
     */
    private String disabilityType;

    /**
     * The TextView stores the value of the SeekBars current location (1-10 in string format)
     */
    private TextView seekBarData;

    /**
     * The ImageView at the top of the activity which should display the disability icon
     */
    private ImageView displayIcon;

    /**
     * The date passed over from the selection page which is fetched by intent
     */
    private String entryDate;

    /**
     * Stores the intent passed from the selection screen. Holds date, and disability type
     */
    private Intent fromSelection;

    /**
     * Holds the default SeekBar value
     */
    private static final String DEFAULT_SEEK_VALUE = "0";

    /**
     * LinearLayout which is a placeholder for a questions that will be loaded in
     */
    private LinearLayout questions;

    /**
     * Database opener
     */
    private DatabaseOpener opener;

    /**
     * SQLiteDatabase
     */
    private SQLiteDatabase db;


    /**
     * onCreate does
     * 1) Initializes values
     * 2) Declares a SeekBar and set the value to 0.  This will  be contained in every question regardless of selection
     * 3) Adds functionality to the submit button when clicked
     * 4) Loads the intent
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //1) Initialization
        displayIcon = findViewById(R.id.selectedIcon);
        questions = findViewById(R.id.questions);
        opener = new DatabaseOpener(this);
        db = opener.getWritableDatabase();

        //2) Declare a SeekBar and implement actions. Set SeekBar value to 0 by default
        SeekBar bar = findViewById(R.id.seekBar);
        setSeekBarActions(bar);
        seekBarData = findViewById(R.id.seekBarData);
        seekBarData.setText(DEFAULT_SEEK_VALUE);

        //3) Adds an OnClickListener to the submit button
        Button submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(e -> submit());

        //4) Loads intent
        fromSelection = getIntent();
        loadIntent();

    }

    /**
     * Loads the intent from the main page. The following things are processed in this method
     * Icon, Date, Questions
     */
    private void loadIntent() {
        disabilityType = fromSelection.getStringExtra(MainActivity.DISABILITY_TYPE);
        entryDate = fromSelection.getStringExtra(MainActivity.DATE);

        //The only reason the values should be null is during testing, however null cases set today, and vision to default
        if(disabilityType == null) disabilityType = MainActivity.VISION;
        if(entryDate == null) entryDate = ListViewActivity.createDateString(Calendar.getInstance().get(Calendar.YEAR), (Calendar.getInstance().get(Calendar.MONTH)+1), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        Log.d("loadIntent date",entryDate );
        Log.d("loadIntent disability", disabilityType);


        //Loads icon and question layout based on disability type
        switch (disabilityType) {
            case MainActivity.SPEAKING:
                displayIcon.setImageResource(R.drawable.speaking);
                break;
            case MainActivity.HEARING:
                displayIcon.setImageResource(R.drawable.hearing);
                break;
            case MainActivity.WALKING:
                displayIcon.setImageResource(R.drawable.walking);
                break;
            case MainActivity.ELIMINATING:
                displayIcon.setImageResource(R.drawable.eliminating);
                break;
            case MainActivity.FEEDING:
                displayIcon.setImageResource(R.drawable.feeding);
                break;
            case MainActivity.DRESSING:
                displayIcon.setImageResource(R.drawable.dressing);
                break;
            case MainActivity.MENTAL:
                displayIcon.setImageResource(R.drawable.mental);
                break;
            default: //default case will act like Vision
                displayIcon.setImageResource(R.drawable.vision);
                break;
        }
    }

    /**
     * Saves the disabilityType, severity, and date
     */
    private void submit() {
            TextView bar = findViewById(R.id.seekBarData);
            if (disabilityType != null && bar.getText() != null && entryDate != null) {
                int severity = Integer.parseInt(bar.getText().toString());
                ContentValues entryRows = new ContentValues();
                entryRows.put(DatabaseOpener.COL_DISABILITY, disabilityType);
                entryRows.put(DatabaseOpener.COL_RATING, severity);
                entryRows.put(DatabaseOpener.COL_DATE, entryDate);
                entryRows.put(DatabaseOpener.COL_FK_PROFILE, HomeActivity.currentProfileId);

                long entryId = db.insert(DatabaseOpener.DISABILITY_TABLE_NAME, null, entryRows);
                TextView notes = findViewById(R.id.noteBox);
                String noteString = notes.getText().toString();
                if( noteString != null) {
                    opener.insert(db, "Note",noteString, entryId);
                }
                startActivity(new Intent(QuestionActivity.this, ListViewActivity.class));

            } else {
                Log.d("Questions", "Error Checking");
            }
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();

    }

    /**
     * Implements OnSeekBarChangedListener methods to track SeekBar data
     */
    private void setSeekBarActions(SeekBar seekbar) {
        seekbar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Not needed
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                String display = String.valueOf(progress/10);
                seekBarData.setText(display);
            }});
    }
}
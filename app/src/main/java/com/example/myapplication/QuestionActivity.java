package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;


public class QuestionActivity extends AppCompatActivity {

    /**
     * String which will hold the name of the disability the page is based on
     */
    private String disabilityType;

    /**
     * SeekBar which allows user to drag
     */
    private SeekBar bar;

    /**
     * The TextView stores the value of the SeekBars current location (1-10 in string format)
     */
    private TextView seekBarData;

    /**
     * The ImageView at the top of the activity which should display the disability icon
     */
    private ImageView displayIcon;

    /**
     * The button that will do submit functions when clicked
     */
    private Button submit;

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
    private PrototypeOneDBOpener opener;

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
        opener = new PrototypeOneDBOpener(this);
        db = opener.getWritableDatabase();

        //2) Declare a SeekBar and implement actions. Set SeekBar value to 0 by default
        bar = findViewById(R.id.seekBar);
        setSeekBarActions(bar);
        seekBarData = findViewById(R.id.seekBarData);
        seekBarData.setText(DEFAULT_SEEK_VALUE);

        //3) Adds an OnClickListener to the submit button
        submit = findViewById(R.id.button);
        submit.setOnClickListener(e -> submitPrototype());

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
        if(entryDate == null) entryDate =
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);

        Log.d("loadIntent date",entryDate );
        Log.d("loadIntent disability", disabilityType);

        //initialize default layout file to be vision
        int defaultQuestion = R.layout.questions_vision;

        //Loads icon and question layout based on disability type
        switch (disabilityType) {
            case MainActivity.SPEAKING:
                displayIcon.setImageResource(R.drawable.speaking);
                defaultQuestion = R.layout.questions_speaking;
                break;
            case MainActivity.HEARING:
                displayIcon.setImageResource(R.drawable.hearing);
                defaultQuestion = R.layout.questions_hearing;
                break;
            case MainActivity.WALKING:
                displayIcon.setImageResource(R.drawable.walking);
                defaultQuestion = R.layout.questions_walking;
                break;
            case MainActivity.ELIMINATING:
                displayIcon.setImageResource(R.drawable.eliminating);
                defaultQuestion = R.layout.questions_eliminating;
                break;
            case MainActivity.FEEDING:
                displayIcon.setImageResource(R.drawable.feeding);
                defaultQuestion = R.layout.questions_feeding;
                break;
            case MainActivity.DRESSING:
                displayIcon.setImageResource(R.drawable.dressing);
                defaultQuestion = R.layout.questions_dressing;
                break;
            case MainActivity.MENTAL:
                displayIcon.setImageResource(R.drawable.mental);
                defaultQuestion = R.layout.questions_mental;
                break;
            default: //default case will act like Vision
                displayIcon.setImageResource(R.drawable.vision);
                break;
        }
        //Adds the question layout to questions
        questions.addView(getLayoutInflater().inflate(defaultQuestion, null));
    }

    /**
     * Saves the disabilityType, severity, and date
     */
    private void submitPrototype() {
        TextView bar = findViewById(R.id.seekBarData);
        if(disabilityType != null && bar.getText() != null && entryDate != null) {
            int severity = Integer.parseInt(bar.getText().toString());
            opener.insert(db, disabilityType, severity, entryDate);
            startActivity(new Intent(QuestionActivity.this, SummaryActivity.class));
            Toast.makeText(this, "Entry Saved", Toast.LENGTH_LONG).show();
        } else {
            //Some sort of error checking for values
        }

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
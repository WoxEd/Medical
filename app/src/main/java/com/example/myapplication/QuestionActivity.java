package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RadioButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Queue;

public class QuestionActivity extends AppCompatActivity {

    private String disabilityType;
    DBHelper QuestionsDB;
    SeekBar bar;
    RadioGroup radioGroup, radioGroup1, radioGroup2;
    RadioButton radioBtn1;
    RadioButton radioBtn2, radioBtn3, radioBtn4, radioBtn5, radioBtn6;
    boolean isChecked, isSelected;
    private int id;
    public static final String DATE_FORMAT = "dd MMMM yyyy";
    public static final String TIME_FORMAT = "hh:mm a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Loads the type of disability passed from the previous page
        loadIntent();

        //Temporary radioGroup which holds a default question that will appear on every disability
         radioGroup = findViewById(R.id.defaultRadio);
        radioGroup1 = findViewById(R.id.Vision1);
        radioGroup2 = findViewById(R.id.Vision2);
        radioBtn1 = findViewById(R.id.questionOneA);
        radioBtn2 = findViewById(R.id.questionOneB);
        radioBtn3 = findViewById(R.id.button1);
        radioBtn4 = findViewById(R.id.button2);
        radioBtn5 = findViewById(R.id.button3);
        radioBtn6 = findViewById(R.id.button4);
        QuestionsDB = new DBHelper(this);
        //Seekbar for selecting severity 1-10
        bar = findViewById(R.id.seekBar);
        setSeekBarActions(bar);

        //Submit button which will process the answers
        Button button = findViewById(R.id.button);
        button.setOnClickListener(e -> {
            submitData();
            //addClickFunction();
        });

    }
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+5"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public static String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+5"));
        Date today = Calendar.getInstance().getTime();
        return timeFormat.format(today);
    }
    public void addClickFunction() {
        Intent goToQuestions = new Intent(QuestionActivity.this, SummaryActivity.class);
        startActivity(goToQuestions);
    }
    /**
     * Loads the intent from the main page and sets the global variable disabilityType to that intent
     * Using a decision structure it chooses the appropriate image and the questions based on the disabilityType
     */
    private void loadIntent() {
        //Intent passed from the main page
        Intent fromMain = getIntent();
        //ImageView for the icon on the top of the questions page
        ImageView image = findViewById(R.id.selectedIcon);


        //Fetches the string from the Intent. If it's null it will be vision by default
        disabilityType = (MainActivity.DISABILITY_TYPE == null) ? MainActivity.VISION : fromMain.getStringExtra(MainActivity.DISABILITY_TYPE);

        //LinearLayout questions will hold the questions from specific disability layout files

        if(fromMain.getStringExtra(MainActivity.DISABILITY_TYPE) == null) {
            disabilityType = MainActivity.VISION;
            id = 1;
        }
        else {
            disabilityType = fromMain.getStringExtra(MainActivity.DISABILITY_TYPE);
            id = fromMain.getIntExtra("id", 0);
        }

        LinearLayout questions = findViewById(R.id.questions);

        //initialize default layout file to be vision
        int defaultQuestion = R.layout.questions_vision;

        switch (disabilityType) {
            case MainActivity.SPEAKING:
                image.setImageResource(R.drawable.speaking);
                defaultQuestion = R.layout.questions_speaking;
                break;
            case MainActivity.HEARING:
                image.setImageResource(R.drawable.hearing);
                defaultQuestion = R.layout.questions_hearing;
                break;
            case MainActivity.WALKING:
                image.setImageResource(R.drawable.walking);
                defaultQuestion = R.layout.questions_walking;
                break;
            case MainActivity.ELIMINATING:
                image.setImageResource(R.drawable.eliminating);
                defaultQuestion = R.layout.questions_eliminating;
                break;
            case MainActivity.FEEDING:
                image.setImageResource(R.drawable.feeding);
                defaultQuestion = R.layout.questions_feeding;
                break;
            case MainActivity.DRESSING:
                image.setImageResource(R.drawable.dressing);
                defaultQuestion = R.layout.questions_dressing;
                break;
            case MainActivity.MENTAL:
                image.setImageResource(R.drawable.mental);
                defaultQuestion = R.layout.questions_mental;
                break;
            default: //default case will act like Vision
                image.setImageResource(R.drawable.vision);
                break;
        }
        View questionView = getLayoutInflater().inflate(defaultQuestion, null);
        questions.addView(questionView);
    }

    
    /**
     * TODO: Implement a submit button on the questions
     */
    private void submitData() {
        if (id == 1) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            int selectedId1 = radioGroup1.getCheckedRadioButtonId();
            int selectedId2 = radioGroup2.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioBtn1 = (RadioButton) findViewById(selectedId);
            radioBtn3 = (RadioButton) findViewById(selectedId1);
            radioBtn5 = (RadioButton) findViewById(selectedId2);
            TextView seekTextView = findViewById(R.id.seekBarData);
            String firstRadioButtonString = radioBtn1.getText().toString();
            int seekBarData;
            if (seekTextView.getText().toString().equals("")) {
                seekBarData = 0;
            } else {
                seekBarData = Integer.parseInt(seekTextView.getText().toString());

            }
            String secondRadioButtonString = radioBtn3.getText().toString();
            String thirdRadioButtonString = radioBtn5.getText().toString();
            String data = firstRadioButtonString + "," + seekBarData + "," + secondRadioButtonString + "," + thirdRadioButtonString;
            Boolean result = QuestionsDB.addQuestionData(data, id,getCurrentDate()+" "+getCurrentTime());
            if (result) {
                Toast.makeText(this, "Inserted Values are " + firstRadioButtonString + "," + seekBarData + "," + secondRadioButtonString + "," + thirdRadioButtonString+" "+getCurrentDate()+" "+getCurrentTime(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(QuestionActivity.this, SummaryActivity.class));
            }
        } else {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioBtn1 = (RadioButton) findViewById(selectedId);

            TextView seekTextView = findViewById(R.id.seekBarData);
            String firstRadioButtonString = radioBtn1.getText().toString();
            int seekBarData;
            if (seekTextView.getText().toString().equals("")) {
                seekBarData = 0;
            } else {
                seekBarData = Integer.parseInt(seekTextView.getText().toString());

            }
            Boolean result = QuestionsDB.addQuestionData(firstRadioButtonString + "," + seekBarData, id, getCurrentDate() + " " + getCurrentTime());
            if (result) {
                Toast.makeText(this, "Inserted Values are " + firstRadioButtonString + "," + seekBarData + " " + getCurrentDate() + " " + getCurrentTime(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(QuestionActivity.this, SummaryActivity.class));
            }
        }

    }

    /**
     * Implements OnSeekBarChangedListener methods to track seekbar data
     */
    private void setSeekBarActions(SeekBar seekbar) {
        //TextView which displays selection 1-10 from seekbar
        TextView data = findViewById(R.id.seekBarData);
        seekbar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Not needed
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Not needed
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                String display = String.valueOf(progress/10);
                data.setText(display);

            }});
    }
}
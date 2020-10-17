package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Queue;

public class QuestionActivity extends AppCompatActivity {

    private String disabilityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Loads the type of disability passed from the previous page
        loadIntent();

        //Temporary radioGroup which holds a default question that will appear on every disability
        RadioGroup radioGroup = findViewById(R.id.defaultRadio);

        //Submit button which will process the answers
        Button button = findViewById(R.id.button);
        button.setOnClickListener(e -> {
//           submitData();
            addClickFunction();
        });

        //Seekbar for selecting severity 1-10
        SeekBar bar = findViewById(R.id.seekBar);
        setSeekBarActions(bar);
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
                defaultQuestion = R.layout.questions_vision;
                break;
        }
        View questionView = getLayoutInflater().inflate(defaultQuestion, null);
        questions.addView(questionView);
    }

    
    /**
     * TODO: Implement a submit button on the questions
     */
    private void submitData() {

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
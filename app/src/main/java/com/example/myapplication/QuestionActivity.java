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

public class QuestionActivity extends AppCompatActivity {

    private String disabilityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Loads the type of disability passed from the previous page
        loadIntent();
        loadImage();
        //Loads the questions based on the disability
        loadQuestions();

        //Temporary radioGroup which holds a default question that will appear on every disability
        RadioGroup radioGroup = findViewById(R.id.defaultRadio);

        //Submit button which will process the answers
        Button button = findViewById(R.id.button);
        button.setOnClickListener(e -> {
           submitData();
        });

        //Seekbar for selecting severity 1-10
        SeekBar bar = findViewById(R.id.seekBar);
        setSeekBarActions(bar);
    }

    /**
     * Loads the intent from the main page and sets the global variable disabilityType to that intent
     */
    private void loadIntent() {
        Intent fromMain = getIntent();
        disabilityType = fromMain.getStringExtra(MainActivity.DISABILITY_TYPE);
    }

    /**
     * Loads the Icon at the top of the page based on the selected disability
     */
    private void loadImage() {
        //This is the icon we will change depending on the disability type loaded...
        ImageView image = findViewById(R.id.selectedIcon);

        switch(disabilityType) {
            case MainActivity.SPEAKING:
                image.setImageResource(R.drawable.speaking);
                break;
            case MainActivity.HEARING:
                image.setImageResource(R.drawable.hearing);
                break;
            case MainActivity.WALKING:
                image.setImageResource(R.drawable.walking);
                break;
            case MainActivity.ELIMINATING:
                image.setImageResource(R.drawable.eliminating);
                break;
            case MainActivity.FEEDING:
                image.setImageResource(R.drawable.feeding);
                break;
            case MainActivity.DRESSING:
                image.setImageResource(R.drawable.dressing);
                break;
            case MainActivity.MENTAL:
                image.setImageResource(R.drawable.mental);
                break;
            default: //default case will act like Vision
                image.setImageResource(R.drawable.vision);
                break;
            //TODO: Create cases for every disability, changing the icon of ImageView accordingly.
        }
    }

    /**
     * Loads the questions based on the disability selected
     */
    private void loadQuestions() {
        //The parent view questions will be populated by the child view based on disabilityType
        LinearLayout questions = findViewById(R.id.questions);
        View questionView;

        switch(disabilityType) {
            case MainActivity.SPEAKING:
                break;
            case MainActivity.HEARING:
                break;
            case MainActivity.WALKING:
                break;
            case MainActivity.ELIMINATING:
                break;
            case MainActivity.FEEDING:
                break;
            case MainActivity.DRESSING:
                break;
            case MainActivity.MENTAL:
                break;
            default: //default case will act like Vision
                questionView = getLayoutInflater().inflate(R.layout.questions_vision, null);
                questions.addView(questionView);
                break;
        }
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
                String display = String.valueOf(progress/=10);
                data.setText(String.valueOf(display));

            }});
    }
}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                break;
            case HEARING:
                goToQuestions.putExtra(DISABILITY_TYPE,HEARING);
                break;
            case WALKING:
                goToQuestions.putExtra(DISABILITY_TYPE,WALKING);
                break;
            case ELIMINATING:
                goToQuestions.putExtra(DISABILITY_TYPE,ELIMINATING);
                break;
            case FEEDING:
                goToQuestions.putExtra(DISABILITY_TYPE,FEEDING);
                break;
            case DRESSING:
                goToQuestions.putExtra(DISABILITY_TYPE,DRESSING);
                break;
            case MENTAL:
                goToQuestions.putExtra(DISABILITY_TYPE,MENTAL);
                break;
            default: //Vision will act as default
                goToQuestions.putExtra(DISABILITY_TYPE,VISION);
                break;
        }
        startActivity(goToQuestions);
    }
}
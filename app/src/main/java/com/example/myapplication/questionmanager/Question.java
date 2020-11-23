package com.example.myapplication.questionmanager;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Horizontal LinearLayout with TextView, Radio Group inside. The purpose of this class is to set the TextView text, add radio group buttons, then add them to the horizontal linear view
 */
public class Question {

    private LinearLayout questionHolder;
    private TextView questionTextView;
    private RadioGroup answerGroup;
    private ArrayList<String> answers;
    private String questionText;
    private Context context;


    public Question(Context context, String questionText, ArrayList<String> answers) {
        this.context = context;
        this.questionText = questionText;
        this.answers = answers;
        questionHolder = new LinearLayout(context);
        questionTextView = new TextView(context);
        questionTextView.setWidth(700);
        answerGroup = new RadioGroup(context);
        answerGroup.setOrientation(LinearLayout.HORIZONTAL);
        setValues();
        addToLayout();
    }

    public TextView getQuestionTextView() {
        return this.questionTextView;
    }

    public RadioGroup getAnswerGroup() {
        return this.answerGroup;
    }

    private void addToLayout() {
        questionHolder.addView(questionTextView);
        questionHolder.addView(answerGroup);
    }

    public LinearLayout getView() {
        return this.questionHolder;
    }

    public void setValues() {
        for(String answer : answers) {
            RadioButton button = new RadioButton(context);
            button.setText(answer);
            button.setVisibility(View.VISIBLE);
            answerGroup.addView(button);
        }

        getQuestionTextView().setText(questionText);
    }


}

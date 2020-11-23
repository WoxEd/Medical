package com.example.myapplication.questionmanager.questionmaker;

import android.content.Context;

import com.example.myapplication.questionmanager.Question;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The purpose of this class is to create a bunch of questions then return them in an ArrayList
 */
public class HearingQuestions extends SuperQuestions{

    private static final String QUESTION_ONE = "Which ear do you experience symptoms in?";
    private static  final ArrayList<String> QUESTION_ONE_ANSWER_SELECTIONS = new ArrayList<>(Arrays.asList("R", "L", "Both"));

    private static final String QUESTION_TWO = "Are you completely deaf?";
    private static  final ArrayList<String> QUESTION_TWO_ANSWER_SELECTIONS = new ArrayList<>(Arrays.asList("Yes", "No"));

    public HearingQuestions(Context context) {
        super(context);
        loadQuestions();
    }

    @Override
    public void loadQuestions() {
        Question question1 = new Question(context, QUESTION_ONE, QUESTION_ONE_ANSWER_SELECTIONS);
        getQuestionList().add(question1);
        Question question2 = new Question(context, QUESTION_TWO, QUESTION_TWO_ANSWER_SELECTIONS);
        getQuestionList().add(question2);
    }

}

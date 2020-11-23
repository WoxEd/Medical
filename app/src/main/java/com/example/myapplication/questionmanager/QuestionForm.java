package com.example.myapplication.questionmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.myapplication.MainActivity;
import com.example.myapplication.PrototypeOneDBOpener;
import com.example.myapplication.questionmanager.questionmaker.DressingQuestions;
import com.example.myapplication.questionmanager.questionmaker.EliminatingQuestions;
import com.example.myapplication.questionmanager.questionmaker.FeedingQuestions;
import com.example.myapplication.questionmanager.questionmaker.HearingQuestions;
import com.example.myapplication.questionmanager.questionmaker.MentalQuestions;
import com.example.myapplication.questionmanager.questionmaker.SpeakingQuestions;
import com.example.myapplication.questionmanager.questionmaker.VisionQuestions;
import com.example.myapplication.questionmanager.questionmaker.WalkingQuestions;

import java.util.ArrayList;

/**
 *  Creates a LinearLayout to populate the QuestionActivity page
 *
 */
public class QuestionForm {

    private LinearLayout questionHolder;
    private Context context;
    private ArrayList<Question> questions;


    public QuestionForm(Context context, String questionType) {
        setContext(context);
        setView(getContext());
        addQuestions(questionType);
    }

    private void setView(Context context) {
        questionHolder = new LinearLayout(context);
        questionHolder.setOrientation(LinearLayout.VERTICAL);
    }

    public LinearLayout getView() {
        return questionHolder;
    }

    private void addQuestions(String questionType) {
        switch (questionType) {
            case MainActivity.SPEAKING:
                questions = new SpeakingQuestions(context).getQuestionList();
                break;
            case MainActivity.HEARING:
                questions = new HearingQuestions(context).getQuestionList();
                break;
            case MainActivity.WALKING:
                questions = new WalkingQuestions(context).getQuestionList();
                break;
            case MainActivity.ELIMINATING:
                questions = new EliminatingQuestions(context).getQuestionList();
                break;
            case MainActivity.FEEDING:
                questions = new FeedingQuestions(context).getQuestionList();
                break;
            case MainActivity.DRESSING:
                questions = new DressingQuestions(context).getQuestionList();
                break;
            case MainActivity.MENTAL:
                questions = new MentalQuestions(context).getQuestionList();
                break;
            default:
                questions = new VisionQuestions(context).getQuestionList();
                break;
        }

        addQuestionsToView(questions);
    }

    private void addQuestionsToView(ArrayList<Question> questions) {
        for(Question question : questions) {
            questionHolder.addView(question.getView());
        }
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private Context getContext() {
        return this.context;
    }

    public void submitQuestions(PrototypeOneDBOpener opener, SQLiteDatabase sqLiteDatabase, long entryId) {
        for(Question question : questions) {
            String questionText = question.getQuestionTextView().getText().toString();
            int selectedId = question.getAnswerGroup().getCheckedRadioButtonId();
            String answerText = ((RadioButton)question.getAnswerGroup().findViewById(selectedId)).getText().toString();
            opener.insert(sqLiteDatabase, questionText, answerText, entryId);
        }
    }

    /**
     * Checks to make sure every radio group has an option selected.
     * @return false if no radio button is selected true if on is
     */
    public boolean isValid() {
        for(Question question : questions) {
            if(question.getAnswerGroup().getCheckedRadioButtonId() == -1){
                return false;
            }
        }
        return true;
    }

}

package com.example.myapplication.questionmanager.questionmaker;

import android.content.Context;

import com.example.myapplication.questionmanager.Question;

import java.util.ArrayList;

abstract class SuperQuestions {

    protected Context context;
    protected ArrayList<Question> questionList;

    public SuperQuestions(Context context) {
        questionList = new ArrayList<>();
        this.context = context;
    }

    abstract void loadQuestions();

    public ArrayList<Question> getQuestionList() {
        return this.questionList;
    }
}

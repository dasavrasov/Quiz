package com.example.android.quiz;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DenisS on 25.09.2017.
 */
class Quiz implements Serializable{
    private String file;
    private String name;
    private String lang;

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    public Quiz(String name, String lang, String file) {
        this.file = file;
        this.name = name;
        this.lang = lang;
    }
}

class Answer implements Serializable{
    private int id;
    private String mText;
    private boolean mRightAnswer;
    private boolean mUserAnswer;

    public int getId() {
        return id;
    }

    public String getText() {
        return mText;
    }


    public boolean isRightAnswer() {
        return mRightAnswer;
    }

    public boolean isUserAnswer() {
        return mUserAnswer;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

//    public void setText(String text) {
//        this.mText = text;
//    }

    public void setRightAnswer(boolean rightAnswer) {
        this.mRightAnswer = rightAnswer;
    }

    public void setUserAnswer(boolean userAnswer) {
        this.mUserAnswer = userAnswer;
    }

    public Answer(int id, String text, boolean rightAnswer) {
        this.id = id;
        this.mText = text;
        this.mRightAnswer = rightAnswer;
    }

}


class Question implements Serializable{
    private String text="";
    private String mExplanation;
    private char typeControl ='0';
    private ArrayList<Answer> answerList;

    public Question(String text, char typeControl, ArrayList<Answer> answerList, String mExplanation) {
        this.text = text;
        this.typeControl = typeControl;
        this.answerList = answerList;
        this.mExplanation = mExplanation;
    }

    public String getText() {
        return text;
    }

    public char getTypeControl() {
        return typeControl;
    }

    public ArrayList<Answer> getAnswerList() {
        return answerList;
    }

    public String getExplanation() {
        return mExplanation;
    }

}

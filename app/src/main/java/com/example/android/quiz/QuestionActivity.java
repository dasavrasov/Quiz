package com.example.android.quiz;

import android.app.Application;
import android.content.res.Configuration;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import static com.example.android.quiz.MainActivity.*;

public class QuestionActivity extends AppCompatActivity {
    private String quizName;
    private String quizTitle;
    private String quizLang;
    private static final String TAG = "MyActivity";
    private int index = 0;
    private int total = 0;
    private LinearLayout questionLayout;
    private Button btnNext;
    private Button btnPrev;
    private Button btnSubmit;
    private TextView questionIndexView;

    private ArrayList<Question> questions_arr = null;
    private boolean isFinished = false; //Quiz finished (FINISH button was clicked)
    //Colors


    private int backgroundColor;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INDEX_KEY,index);
        outState.putSerializable(LIST_KEY,questions_arr);
        outState.putBoolean("Finished",isFinished);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt(INDEX_KEY, 0);
        questions_arr=(ArrayList<Question>) savedInstanceState.getSerializable(LIST_KEY);
        isFinished=savedInstanceState.getBoolean("Finished");
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //add Up button to Action bar
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //get quizName parameter from MainActivity
        Bundle b = getIntent().getExtras();
        quizName= b.getString(QUIZ_NAME);
        quizTitle= b.getString(QUIZ_TITLE);
        quizLang= b.getString(QUIZ_LANG);

        actionBar.setTitle(quizTitle);

        //get Locale depends on quiz
        Locale locale = new Locale(quizLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        backgroundColor=ContextCompat.getColor(this,R.color.background);
        questionLayout = new LinearLayout(this);
        questionLayout = (LinearLayout) findViewById(R.id.questionLayout);
        questionIndexView = (TextView)  findViewById(R.id.question_index);


        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(INDEX_KEY, 0);
            questions_arr=(ArrayList<Question>) savedInstanceState.getSerializable(LIST_KEY);
            isFinished=savedInstanceState.getBoolean("Finished");
        }
        else
        {
            questions_arr = LoadQuestions();
        }
//        else {
            //load english in ArrayList

            if (questions_arr == null || questions_arr.size() == 0) {
                btnNext.setVisibility(View.INVISIBLE);
                btnPrev.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No quiz", Toast.LENGTH_SHORT).show();
                return;
            }
//        }
//        ShowQuestion(questions_arr.get(index), questionLayout);
            UpdateQuestion(index);

    }

    /*
    * creates Views to display quuestion
    * if type of question "RadioGroup" creates RadioGroup
    * otherwise creates Checkboxes
     */
    private void ShowQuestion(Question question, ViewGroup parent) {
        parent.removeAllViews();
        TextView questViewTag = new TextView(this);
        questViewTag.setText(getString(R.string.question)+":");
        questViewTag.setPadding(0,15,0,0);
        parent.addView(questViewTag);

        TextView questView = new TextView(this);
        questView.setText(question.getText());
//        questView.setBackgroundColor(backgroundColor);
//        questView.setBackgroundResource(R.drawable.textvew_border);
//        questView.setElevation(1);
        questView.setPadding(10,10,10,20);

        parent.addView(questView);
        questionIndexView.setText(""+(index+1)+"/"+total);

        char type=question.getTypeControl(); //type of Control
        if (type == 'R') {
            RadioGroup radioView = new RadioGroup(this);
//            radioView.setBackgroundResource(R.drawable.textvew_border);
            radioView.setPadding(10,10,10,20);
            ArrayList<Answer> answers = question.getAnswerList();
            for (final Answer answer : answers) {
                CompoundButton r=AnswerFactory.getInstance().of(type,answer,isFinished,this);
                radioView.addView(r);
            }
            parent.addView(radioView);
        } else {
            ArrayList<Answer> answers = question.getAnswerList();
            for (Answer answer : answers) {
                CompoundButton r=AnswerFactory.getInstance().of(type,answer,isFinished,this);
                parent.addView(r);
            }
        }
        if (isFinished) {
            TextView explanationTag = new TextView(this); //(TextView) findViewById(R.id.question_text_view);
            explanationTag.setText(getString(R.string.explanation)+":");
            explanationTag.setPadding(0,25,0,0);
            TextView explanationView = new TextView(this); //(TextView) findViewById(R.id.question_text_view);
            explanationView.setText(question.getExplanation());
//            questView.setBackgroundColor(backgroundColor);
//            explanationView.setBackgroundResource(R.drawable.textvew_border);
            explanationView.setPadding(10,10,10,20);
            parent.addView(explanationTag);
            parent.addView(explanationView);
        }

    }

    /*
    * Display next question and updates buttons visibility
     */
    private void UpdateQuestion(int index) {
        ShowQuestion(questions_arr.get(index), questionLayout);

        if (index == questions_arr.size() - 1) {
            btnNext.setVisibility(View.INVISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.INVISIBLE);
        }

        if (index == 0)
            btnPrev.setVisibility(View.INVISIBLE);
        else
            btnPrev.setVisibility(View.VISIBLE);

        Log.v(TAG,"UpdateQuestion "+index);
    }

    /*
    * checked user answer marks and store field userAnswer in answerList
     */
    private void UpdateAnswer(Question question) {
        ArrayList<Answer> answers = question.getAnswerList();
        for (Answer answer : answers) {
            CompoundButton button = (CompoundButton) findViewById(answer.getId());
//            if (button==null)
//                return;
            if (
                    button.isChecked())
                answer.setUserAnswer(true);
            else
                answer.setUserAnswer(false);

        }
    }

    /*
    * Move index to next question
     */
    public void btnNextOnClick(View view) {
        if (index == questions_arr.size() - 1)
            return;
        UpdateAnswer(questions_arr.get(index));
        UpdateQuestion(++index);
    }

    /*
    * Move index to previous question
     */
    public void btnPrevOnClick(View view) {
        if (index == 0)
            return;
        UpdateAnswer(questions_arr.get(index));
        UpdateQuestion(--index);
    }

    /*
    * Finish button pressed. User answers submited
    * Counts correct and incorrect answers
     */
    public void btnSubmitOnClick(View view) {
        int err = 0;

        isFinished = true;
        UpdateAnswer(questions_arr.get(index));
        UpdateQuestion(index);

        for (int i = 0; i < questions_arr.size(); i++) {
//            Log.v(TAG, "question " + i);
            ArrayList<Answer> answers = questions_arr.get(i).getAnswerList();
            for (Answer answer : answers) {
//                Log.v(TAG, "answer " + answer.isRightAnswer());
                if (answer.isUserAnswer() != answer.isRightAnswer()) {
                    err++;
                    break;
                }

            }

        }
//        Log.v(TAG, "Errors " + err);
//        Log.v(TAG, "Result " + String.format("%d", err / questions_arr.size() * 100));
        Toast toast = Toast.makeText(this, getString(R.string.correct)+":" + (questions_arr.size() - err) + "\n" + getString(R.string.wrong)+":" + err, Toast.LENGTH_SHORT);
        toast.show();
    }


    /*
    * Loads questions list to ArrayList from XML resource file, placed in raw folder
     */
    private ArrayList<Question> LoadQuestions() {
        // import english from XML file to ArrayList
//            Log.v(TAG, "LoadQuestions ");
//        InputStream in = getResources().openRawResource(R.raw.english);
        try {


            int qId = getResources().getIdentifier(quizName.toLowerCase(), "raw", getPackageName());


            InputStream in = getResources().openRawResource(qId);

            ArrayList<Question> questions = QXMLParser.LoadFromQuestions(in);
            total = questions.size();
            return questions;
//        Log.v(TAG, "english.size " + english.size());

//************************

//// 1 question
//        ArrayList<Question> english = new ArrayList<>();
//
//        ArrayList<Answer> answerList = new ArrayList<>();
//        answerList.add(new Answer(10, "Yes", true));
//        answerList.add(new Answer(20, "No", false));
//        answerList.add(new Answer(30, "I think so", false));
//        Question question = new Question("I am a good developer", 'R', answerList);
//        english.add(question);
//
//// 2 question
//        ArrayList<Answer> answerList2 = new ArrayList<>();
//        answerList2.add(new Answer(11, "Cow", true));
//        answerList2.add(new Answer(22, "Hen", false));
//        answerList2.add(new Answer(33, "Tiger", false));
//        Question question2 = new Question("Find farm animals", 'C', answerList2);
//        english.add(question2);
//

        }catch (Exception e){
            return null;
        }

    }

}

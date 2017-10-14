package com.example.android.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String INDEX_KEY="Index";
    static final String LIST_KEY="QuestionList";
    static final String QUIZ_NAME="quizName";
    static final String QUIZ_TITLE="quizTitle";
    static final String QUIZ_LANG="quizLang";

    private ArrayAdapter<String> mAdapter;
    ArrayList<Quiz> quizes;
    ArrayList<String> quizNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quizes=LoadQuizes();
        quizNames=new ArrayList<>();
        quizNames.clear();
        for (Quiz quiz:quizes
             ) {
            quizNames.add(quiz.getName());
        }

        ListView quizList=(ListView) findViewById(R.id.quiz_list_view);


        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, quizNames);

        quizList.setAdapter(mAdapter);
        quizList.setOnItemClickListener(qOnItemClickListener);

    }


    private ArrayList<Quiz> LoadQuizes() {
        InputStream in = getResources().openRawResource(R.raw.quizes);
        ArrayList<Quiz> quizes = QXMLParser.LoadFromQuizList(in);
        int total=quizes.size();
//        System.out.println("Read "+total);
//        Log.v(TAG, "english.size " + english.size());

        return quizes;
    }

    AdapterView.OnItemClickListener qOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
        //find нужный quiz by Id
//            Toast.makeText(getApplicationContext(),
//                    "Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();
//            QuestionActivity.quizName=quizes.get(position);
            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
            intent.putExtra(QUIZ_NAME,quizes.get(position).getFile());
            intent.putExtra(QUIZ_LANG,quizes.get(position).getLang());
            intent.putExtra(QUIZ_TITLE,quizes.get(position).getName());
            startActivity(intent);

        }
    };


}

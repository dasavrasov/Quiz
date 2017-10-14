package com.example.android.quiz;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by DenisS on 07.10.2017.
 */

class AnswerFactory {

    private static final AnswerFactory instance=new AnswerFactory();

    private AnswerFactory(){};

    //singleton
    public static AnswerFactory getInstance(){
            return instance;
    }

    private void UpdateTextColor(CompoundButton button, Answer answer, boolean finished, Context context) {
        if (finished) {
            if (answer.isRightAnswer()) {
                int color = ContextCompat.getColor(context,R.color.correctAnswer);
                button.setTextColor(color);
//                button.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
            }
            else if (answer.isUserAnswer()) {
                int color = ContextCompat.getColor(context,R.color.wrongAnswer);
                button.setTextColor(color);
//                button.setButtonTintList(ColorStateList.valueOf(Color.RED));
            }
            else
            {
//                Log.v(TAG, "Radio: Set Color to BLACK");
                button.setTextColor(Color.BLACK);
//                button.setButtonTintList(ColorStateList.valueOf(Color.BLACK));

            }
        }
    }

    CompoundButton of(char type, Answer answer, boolean finished, Context context){
        CompoundButton r;
        if (type=='R')
            r = new RadioButton(context);
        else
            r = new CheckBox(context);

        r.setId(answer.getId());
        r.setText(answer.getText());
        r.setChecked(answer.isUserAnswer());
        UpdateTextColor(r, answer, finished, context);

        return r;
    }
}

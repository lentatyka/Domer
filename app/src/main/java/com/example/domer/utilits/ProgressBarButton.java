package com.example.domer.utilits;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.domer.R;

public class ProgressBarButton {
    private final CardView cardView;
    private final ConstraintLayout constraintLayout;
    private final ProgressBar progressBar;
    private final TextView textView;

    Animation fade_in;

     public ProgressBarButton(Context context, View v){
         fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        cardView = v.findViewById(R.id.card_layout);
        constraintLayout = v.findViewById(R.id.constrain_layout);
        progressBar = v.findViewById(R.id.pBar);
        textView = v.findViewById(R.id.textView2);
    }

    public void buttonActivated(){
         constraintLayout.setBackgroundColor(cardView.getResources()
                 .getColor(R.color.purple_500, null));
         progressBar.setAnimation(fade_in);
         textView.setAnimation(fade_in);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(R.string.wait);
    }

    public void buttonFinished(int str){
        textView.setText(str);
        progressBar.setVisibility(View.GONE);

        if(str == R.string.success)
            constraintLayout.setBackgroundColor(Color.GREEN);
        else
            constraintLayout.setBackgroundColor(Color.RED);
    }
}

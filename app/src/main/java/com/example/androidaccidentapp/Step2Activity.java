package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Step2Activity extends AppCompatActivity {
    boolean firstSegment;
    boolean secondSegment;
    boolean thirdSegment;
    boolean fourthSegment;

    TextView firstTextView;
    TextView secondTextView;
    TextView thirdTextView;
    TextView fourthTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);

        //initialize text views to allow to set visibilities
        firstTextView = (TextView)findViewById(R.id.step2firstTv);
        secondTextView = (TextView)findViewById(R.id.step2secondTv);
        thirdTextView = (TextView)findViewById(R.id.step2thirdTv);
        fourthTextView = (TextView)findViewById(R.id.step2fourthTv);

        //firstTextView will be visible when activity first created
        firstTextView.setVisibility(View.VISIBLE);

        //when activity first created user will see first portion of text
        firstSegment = true;

        //this code changes action bar on top to have logo and specified background color
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo_02);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
    }

    /*
    the arrow forward and
    back methods are a little ugly
    so I will try and clean that up

     */

    //left arrow click
    public void goBack(View view) {
        if(firstSegment){
            changeToPreviousActivity(view);
        }

        //to do finish other segments
        else if(secondSegment){
            firstTextView.setVisibility(View.VISIBLE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.GONE);
            secondSegment = false;
            thirdSegment = false;
            fourthSegment = false;
            firstSegment = true;
        }
        else if(thirdSegment){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.VISIBLE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.GONE);
            secondSegment = true;
            thirdSegment = false;
            fourthSegment = false;
            firstSegment = false;
        }
        else if(fourthSegment){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.VISIBLE);
            fourthTextView.setVisibility(View.GONE);
            secondSegment = false;
            thirdSegment = true;
            fourthSegment = false;
            firstSegment = false;
        }
        else{
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.VISIBLE);
            secondSegment = false;
            thirdSegment = false;
            fourthSegment = true;
            firstSegment = false;
        }
    }

    //right arrow click
    public void goForward(View view) {
        if(firstSegment){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.VISIBLE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.GONE);
            firstSegment = false;
            secondSegment = true;
            thirdSegment = false;
            fourthSegment = false;
        }
        else if(secondSegment){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.VISIBLE);
            fourthTextView.setVisibility(View.GONE);
            firstSegment = false;
            secondSegment = false;
            thirdSegment = true;
            fourthSegment = false;
        }
        else if(thirdSegment){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.VISIBLE);
            firstSegment = false;
            secondSegment = false;
            thirdSegment = false;
            fourthSegment = true;
        }
        else{
            changeToNextActivity(view);
        }
    }

    public void changeToPreviousActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public void changeToNextActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }


}
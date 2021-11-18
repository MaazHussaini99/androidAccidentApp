package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Step1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);
    }

    public void previous(View view) {
        Intent intentPrev = new Intent(Step1.this, Home.class);
        startActivity(intentPrev);
    }

    public void next(View view) {
        Intent intentNxt = new Intent(Step1.this, UserProfile.class);
        startActivity(intentNxt);
    }
}
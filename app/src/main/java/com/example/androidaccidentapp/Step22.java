package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Step22 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step22);
    }

    public void next(View view) {
        Intent intent = new Intent(this, Step5.class);
        this.startActivity(intent);
    }

    public void previous(View view) {
        Intent intent = new Intent(this, Step1.class);
        this.startActivity(intent);
    }
}
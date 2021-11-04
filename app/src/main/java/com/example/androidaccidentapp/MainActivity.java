package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //test step 2 activity
    public void testStep2(View view) {
        Intent intent = new Intent(this, Step2Activity.class);
        this.startActivity(intent);
    }
}
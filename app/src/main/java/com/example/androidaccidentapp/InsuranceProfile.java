package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InsuranceProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_profile);
    }

    public void save(View view) {
        Intent intent = new Intent(InsuranceProfile.this, Home.class);
        startActivity(intent);
    }
}
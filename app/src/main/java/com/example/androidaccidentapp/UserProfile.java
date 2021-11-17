package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public void edit_vehicle(View view) {
        Intent intent = new Intent(UserProfile.this, VehicleProfile.class);
        startActivity(intent);
    }

    public void edit_insurance(View view) {
        Intent intent = new Intent(UserProfile.this, InsuranceProfile.class);
        startActivity(intent);

    }

    public void save(View view) {
        Intent intent = new Intent(UserProfile.this, Home.class);
        startActivity(intent);
    }

}
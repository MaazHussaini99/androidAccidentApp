package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VehicleDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);



        Button saveVehicle = (Button)findViewById(R.id.saveVehicle);
        saveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeToNextActivity(view);
            }
        });
    }

    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, SavedVehicles.class);
        this.startActivity(intent);
    }
}
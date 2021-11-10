package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class infoExchange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoexchange);


        Button btn1;
        btn1 = (Button) findViewById(R.id.saveInsuranceInfo);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToNextActivity(view);
            }
        });
    }


    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, VehicleDetails.class);
        this.startActivity(intent);
    }
}
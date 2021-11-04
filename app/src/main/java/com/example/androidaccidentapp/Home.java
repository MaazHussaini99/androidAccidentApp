package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageView userAcct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userAcct = findViewById(R.id.account_ic);

//        userAcct.setOnClickListener(v -> {
//            AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//            //Add message on alert
//            alert.setMessage("User Account Options Test");
//
//            //Add positive button
//            alert.setPositiveButton("Agree", (view,a) -> {
//                Toast.makeText(Home.this, "You Clicked Agree on the Alert", Toast.LENGTH_LONG).show();
//            });
//
//            alert.setNegativeButton("Disagree", (view,a) -> {
//                Toast.makeText(Home.this, "You Clicked Disagree on the Alert", Toast.LENGTH_LONG).show();
//            });
//
//            alert.create().show();
//        });

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.step_by_step:{
                Intent myIntent = new Intent(Home.this, StepByStep.class);
                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.handbook:{
                Intent myIntent = new Intent(Home.this, Handbook.class);
                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.camera:{
                Intent myIntent = new Intent(Home.this, Camera.class);
                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.info_exchange:{
                Intent myIntent = new Intent(Home.this, InfoExchange.class);
                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.reports:{
                Intent myIntent = new Intent(Home.this, Reports.class);
                Home.this.startActivity(myIntent);
                break;
            }
        }
    }
}
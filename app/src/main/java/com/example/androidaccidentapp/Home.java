package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageView userAcct;
    Button handbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userAcct = findViewById(R.id.account_ic);

        userAcct.setOnClickListener(v -> {
            AlertDialog.Builder profileDialog = new AlertDialog.Builder(this);

            //Set User Profile Dialog Title
            profileDialog.setTitle("User Account Options Test");

            //Login Button
            profileDialog.setPositiveButton("Sign In", (view,a) -> {
                Intent intent = new Intent(Home.this, Registration.class);
                startActivity(intent);
            });

            profileDialog.create().show();
        });
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.step_by_step:{
                Toast.makeText(Home.this, "Step-By-Step", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, StepByStep.class);
//                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.handbook:{
                Toast.makeText(Home.this, "Handbook", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, Handbook.class);
//                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.camera:{
                Toast.makeText(Home.this, "Camera", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, Camera.class);
//                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.info_exchange:{
                Toast.makeText(Home.this, "Info Exchange", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, InfoExchange.class);
//                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.reports:{
                Toast.makeText(Home.this, "Reports", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, Reports.class);
//                Home.this.startActivity(myIntent);
                break;
            }
        }
    }
}
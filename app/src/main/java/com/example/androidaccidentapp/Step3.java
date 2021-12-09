package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Step3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
    }

    public void next(View view) {
        Intent intent = new Intent(this, driverInfo.class);
        this.startActivity(intent);
    }

    public void previous(View view) {
        Intent intent = new Intent(this, Step22.class);
        this.startActivity(intent);
    }

    public void infoexchangegoto(View view) {
        Intent intent = new Intent(this, driverInfo.class);
        this.startActivity(intent);
    }

}
package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class ProfileVehicle extends AppCompatActivity {

    EditText carMake, yearEdit, plateNumEdit, stateEdit, carTypeEdit;
    Switch editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_vehicle);

        carMake = findViewById(R.id.makeEdit);
        yearEdit = findViewById(R.id.yearEdit);
        plateNumEdit = findViewById(R.id.plateNumEdit);
        stateEdit = findViewById(R.id.stateEdit);
        carTypeEdit = findViewById(R.id.typeEdit);

        editable = findViewById(R.id.editable);
    }

    public void updateText(View view) {
        //Response is slow -- verify correct implementation being used
        editable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(editable.isChecked()){
                    carMake.setEnabled(true);
                    carMake.setTextColor(Color.WHITE);
                    yearEdit.setEnabled(true);
                    yearEdit.setTextColor(Color.WHITE);
                    plateNumEdit.setEnabled(true);
                    plateNumEdit.setTextColor(Color.WHITE);
                    stateEdit.setEnabled(true);
                    stateEdit.setTextColor(Color.WHITE);
                    carTypeEdit.setEnabled(true);
                    carTypeEdit.setTextColor(Color.WHITE);
                }else{
                    carMake.setEnabled(false);
                    carMake.setTextColor(Color.GRAY);
                    yearEdit.setEnabled(false);
                    yearEdit.setTextColor(Color.GRAY);
                    plateNumEdit.setEnabled(false);
                    plateNumEdit.setTextColor(Color.GRAY);
                    stateEdit.setEnabled(false);
                    stateEdit.setTextColor(Color.GRAY);
                    carTypeEdit.setEnabled(false);
                    carTypeEdit.setTextColor(Color.GRAY);
                }
            }
        });

    }

    public void edit_profile(View view) {
        Intent intent = new Intent(ProfileVehicle.this, ProfileUser.class);
        startActivity(intent);
    }

    public void edit_Insurance(View view) {
        Intent intent = new Intent(ProfileVehicle.this, ProfileInsurance.class);
        startActivity(intent);
    }

    public void save(View view) {
        //push updated data over to firebase
        carMake.setEnabled(false);
        carMake.setTextColor(Color.GRAY);
        yearEdit.setEnabled(false);
        yearEdit.setTextColor(Color.GRAY);
        plateNumEdit.setEnabled(false);
        plateNumEdit.setTextColor(Color.GRAY);
        stateEdit.setEnabled(false);
        stateEdit.setTextColor(Color.GRAY);
        carTypeEdit.setEnabled(false);
        carTypeEdit.setTextColor(Color.GRAY);
        editable.setChecked(false);
    }


}
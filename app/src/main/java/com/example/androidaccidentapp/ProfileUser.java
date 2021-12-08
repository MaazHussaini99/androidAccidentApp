package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class ProfileUser extends AppCompatActivity {

    EditText firstName, lastName, dob, address, license, iceContact;
    Switch editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        firstName = findViewById(R.id.firstNameEdit);
        lastName = findViewById(R.id.lastNameEdit);
        dob = findViewById(R.id.dobEdit);
        address = findViewById(R.id.addressEdit);
        license = findViewById(R.id.licenseEdit);
        iceContact = findViewById(R.id.iceContactEdit);

        editable = findViewById(R.id.editable);
    }

    public void edit_vehicle(View view) {
        Intent intent = new Intent(ProfileUser.this, ProfileVehicle.class);
        startActivity(intent);
    }

    public void edit_insurance(View view) {
        Intent intent = new Intent(ProfileUser.this, ProfileInsurance.class);
        startActivity(intent);

    }

    public void updateText(View view) {
        editable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (editable.isChecked()) {
                    firstName.setEnabled(true);
                    firstName.setTextColor(Color.WHITE);
                    lastName.setEnabled(true);
                    lastName.setTextColor(Color.WHITE);
                    dob.setEnabled(true);
                    dob.setTextColor(Color.WHITE);
                    address.setEnabled(true);
                    address.setTextColor(Color.WHITE);
                    license.setEnabled(true);
                    license.setTextColor(Color.WHITE);
                    iceContact.setEnabled(true);
                    iceContact.setTextColor(Color.WHITE);
                } else {
                    firstName.setEnabled(false);
                    firstName.setTextColor(Color.GRAY);
                    lastName.setEnabled(false);
                    lastName.setTextColor(Color.GRAY);
                    dob.setEnabled(false);
                    dob.setTextColor(Color.GRAY);
                    address.setEnabled(false);
                    address.setTextColor(Color.GRAY);
                    license.setEnabled(false);
                    license.setTextColor(Color.GRAY);
                    iceContact.setEnabled(false);
                    iceContact.setTextColor(Color.GRAY);
                }
            }
        });
    }

    public void save(View view) {
        //push updated data over to firebase
        firstName.setEnabled(false);
        firstName.setTextColor(Color.GRAY);
        lastName.setEnabled(false);
        lastName.setTextColor(Color.GRAY);
        dob.setEnabled(false);
        dob.setTextColor(Color.GRAY);
        address.setEnabled(false);
        address.setTextColor(Color.GRAY);
        license.setEnabled(false);
        license.setTextColor(Color.GRAY);
        iceContact.setEnabled(false);
        iceContact.setTextColor(Color.GRAY);
        editable.setChecked(false);
    }
}
package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    EditText firstName, lastName, dob, address, license, iceContact;
    TextView editLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firstName = findViewById(R.id.firstNameEdit);
        lastName = findViewById(R.id.lastNameEdit);
        dob = findViewById(R.id.dobEdit);
        address = findViewById(R.id.addressEdit);
        license = findViewById(R.id.licenseEdit);
        iceContact = findViewById(R.id.iceContactEdit);
        editLink = findViewById(R.id.editLink);
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
        Intent intent = new Intent(UserProfile.this, UserProfile.class);
        startActivity(intent);
    }

    public void updateText(View view) {
        editLink.setTextColor(Color.CYAN);
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
    }
}
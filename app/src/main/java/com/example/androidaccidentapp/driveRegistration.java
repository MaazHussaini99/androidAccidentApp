package com.example.androidaccidentapp;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class driveRegistration extends AppCompatActivity {

    EditText fName;
    EditText lName;
    EditText dob;
    EditText address;
    EditText licence;
    EditText iceContact;
    Button nxt;


    String firstName;
    String lastName;
    String dateOfBirth;
    String addressDriver;
    String licenceNum;
    String iceNum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_registration);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        licence = findViewById(R.id.driveLicence);
        iceContact = findViewById(R.id.icePhone);
        nxt = findViewById(R.id.saveInsuranceInfo);



        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = fName.getText().toString();
                lastName = lName.getText().toString();
                dateOfBirth = dob.getText().toString();
                addressDriver = address.getText().toString();
                licenceNum = licence.getText().toString();
                iceNum = iceContact.getText().toString();

                if (isEmpty(firstName) || TextUtils.isEmpty(lastName) || (TextUtils.isEmpty(dateOfBirth)
                        || (TextUtils.isEmpty(addressDriver) || TextUtils.isEmpty(licenceNum)))) {
                    Toast.makeText(driveRegistration.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if (firstName.length() < 3 || firstName.length() > 30 || //makes sure first name and last name are within 3 to 30 chars
                        lastName.length() < 3 || lastName.length() > 30){
                    Toast.makeText(driveRegistration.this, "First name and last name must be between 3 and 30 characters", Toast.LENGTH_SHORT).show();
                }
                else if (licenceNum.length() < 8) {
                    Toast.makeText(driveRegistration.this, "License should be at least 8 characters", Toast.LENGTH_SHORT).show();
                }
                else{
                    changeToNextActivity(view);
                }
            }
        });

    }



    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, insuranceRegistration.class);
        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);
        intent.putExtra("ICE Contact", iceNum);
        Log.d("iceNum", ""+ iceNum);
        this.startActivity(intent);
    }


}